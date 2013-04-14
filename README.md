# Organisation Flapdoodle OSS

We are now a github organisation. You are invited to participate.

# Log Parser

read any java log format and process it

## Why?

... because i did need it:)

## Howto

**This will need Java 7 runtime, because of the used file io enhancement and java regex named group support.**

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

- documentation enhancement

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

This is how you can parse a simple logfile (with stacktrace support). In this example the logfile can look
like this:

	2013-04-07 00:00:01 INFO: Everything is fine
	2013-04-07 00:00:01 ERROR: Something went wrong
	de.flapdoodle.BadCaseException: wo should stop right now
		at de.flapdoodle.HarmlessComponent.simpleMethod(HarmlessComponent.java:123)
		at de.flapdoodle.Main.main(Main.java:12)
	Caused by: java.lang.NullPointerException: null
		at de.flapdoodle.stuff.ReachTheStarAdapter.reach(ReachTheStarAdapter.java:123456)
		at de.flapdoodle.stuff.ReachSomething.reach(ReachSomething.java:123456)
		... 64 common frames omitted

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
