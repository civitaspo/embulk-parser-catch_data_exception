Embulk::JavaPlugin.register_parser(
  "catch_data_exception", "org.embulk.parser.catch_data_exception.CatchDataExceptionParserPlugin",
  File.expand_path('../../../../classpath', __FILE__))
