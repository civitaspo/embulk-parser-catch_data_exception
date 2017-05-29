package org.embulk.parser.catch_data_exception;

import com.google.common.collect.Lists;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;
import org.embulk.spi.Column;
import org.embulk.spi.DataException;
import org.embulk.spi.Exec;
import org.embulk.spi.FileInput;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.ParserPlugin;
import org.embulk.spi.Schema;
import org.embulk.spi.type.Types;
import org.embulk.spi.util.LineDecoder;

public class CatchDataExceptionParserPlugin
        implements ParserPlugin
{
    public interface PluginTask
            extends Task, LineDecoder.DecoderTask
    {
    }

    @Override
    public void transaction(ConfigSource config, ParserPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);
        Schema schema = new Schema(Lists.newArrayList(new Column(0, "_c0", Types.STRING)));
        control.run(task.dump(), schema);
    }

    @Override
    public void run(TaskSource taskSource, Schema schema,
            FileInput input, PageOutput output)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);
        LineDecoder decoder = new LineDecoder(input, task);
        Column column = schema.getColumns().get(0);

        try (final PageBuilder builder = new PageBuilder(Exec.getBufferAllocator(), schema, output)) {
            long lineNumber;
            String poll;

            while (decoder.nextFile()) {
                lineNumber = 0;
                while ((poll = decoder.poll()) != null) {
                    lineNumber++;

                    try {
                        builder.setString(column, poll);
                        builder.addRecord();
                    }
                    catch (DataException e) {
                        String m = String.format("class: %s, line number: %d", CatchDataExceptionParserPlugin.class.getName(), lineNumber);
                        Exec.getLogger(CatchDataExceptionParserPlugin.class).warn(
                                m + e.getMessage(), e);
                    }
                }
            }

            builder.finish();
        }
    }
}
