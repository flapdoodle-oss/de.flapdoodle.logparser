# Organisation Flapdoodle OSS

We are now a github organisation. You are invited to participate.

# Log Parser

read any java log format and process it

## Why?

... because i did need it:)

## Howto

### Maven

Stable (Maven Central Repository, Released: 09.04.2013 - wait 24hrs for [maven central](http://repo1.maven.org/maven2/de/flapdoodle/de.flapdoodle.logparser/maven-metadata.xml))

	<dependency>
		<groupId>de.flapdoodle</groupId>
		<artifactId>de.flapdoodle.logparser</artifactId>
		<version>1.1</version>
	</dependency>

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

	<dependency>
		<groupId>de.flapdoodle</groupId>
		<artifactId>de.flapdoodle.logparser</artifactId>
		<version>1.2-SNAPSHOT</version>
	</dependency>

### Changelog

#### 1.2 (SNAPSHOT)

#### 1.1

- major refactoring
- multi line message stacktrace support

#### 1.0

- can handle standard java logging and some custom logback format
- supports stack trace parsing

### Usage

With this library it should be easy to setup a parser for most common and custom log files
The following examples will show how you can do this.

#### Simple LogFile

		...
		String regex = "(?<date>\\d+-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d) (?<level>[A-Z]+): (?<message>.*)$";
		Pattern firstLinePattern = Pattern.compile(regex);
		GenericLogMatcher genericLogMatcher = new GenericLogMatcher(firstLinePattern);

		List<IMatcher<LogEntry>> matchers = Lists.<IMatcher<LogEntry>> newArrayList(genericLogMatcher);

		IStreamListener<LogEntry> streamListener = collectingStreamListener;
		ILineProcessor lineProcessor = writeToListLineProcessor;

		StreamProcessor<LogEntry> streamProcessor = new StreamProcessor<LogEntry>(matchers, lineProcessor, streamListener);
		...
		...
		int readAheadLimit = 1024;
		IRewindableReader reader = new BufferedReaderAdapter(inputStream, Charsets.UTF_8, readAheadLimit);
		streamProcessor.process(reader);
		...
