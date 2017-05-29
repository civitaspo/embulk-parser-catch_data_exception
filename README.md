# Catch Data Exception parser plugin for Embulk

TODO: Write short description here and build.gradle file.

## Overview

* **Plugin type**: parser
* **Guess supported**: no

## Configuration

- **option1**: description (integer, required)
- **option2**: description (string, default: `"myvalue"`)
- **option3**: description (string, default: `null`)

## Example

```yaml
in:
  type: any file input plugin type
  parser:
    type: catch_data_exception
    option1: example1
    option2: example2
```

(If guess supported) you don't have to write `parser:` section in the configuration file. After writing `in:` section, you can let embulk guess `parser:` section using this command:

```
$ embulk gem install embulk-parser-catch_data_exception
$ embulk guess -g catch_data_exception config.yml -o guessed.yml
```

## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
