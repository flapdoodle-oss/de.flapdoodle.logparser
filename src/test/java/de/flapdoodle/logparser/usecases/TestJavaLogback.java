/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.usecases;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.flapdoodle.logparser.StreamProcessor;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IRewindableReader;
import de.flapdoodle.logparser.LogEntry;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.WriteToListLineProcessor;
import de.flapdoodle.logparser.matcher.generic.GenericLogMatcher;
import de.flapdoodle.logparser.stacktrace.AbstractStackFrame;
import de.flapdoodle.logparser.stacktrace.At;
import de.flapdoodle.logparser.stacktrace.CauseBy;
import de.flapdoodle.logparser.stacktrace.More;
import de.flapdoodle.logparser.streamlistener.CollectingStreamListener;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestJavaLogback {

	@Test
	public void readLogfile() throws IOException {
		CollectingStreamListener<LogEntry> streamListener = new CollectingStreamListener<LogEntry>();
		WriteToListLineProcessor defaultLineProcessor = new WriteToListLineProcessor();

		try (InputStream stream = getClass().getResourceAsStream("java-logback-stacktrace-sample.txt")) {
			IRewindableReader reader = new BufferedReaderAdapter(stream, Charsets.UTF_8, 1024);

			String regex = "(?<date>\\d+-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) (?<level>[A-Z]+) [a-z]+-[a-z]+-\\d+ \\[\\d+\\] \\[\\d+/[A-Z0-9]+-[a-z]\\d.[a-z]+\\d+[a-z]/\\d+\\] (?<class>([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*):(?<lineNr>\\d+): (?<message>.*)$";
			Pattern firstLinePattern = Pattern.compile(regex);
			StreamProcessor<LogEntry> streamProcessor = new StreamProcessor<LogEntry>(
					Lists.<IMatcher<LogEntry>> newArrayList(new GenericLogMatcher(firstLinePattern)), defaultLineProcessor,
					streamListener);

			streamProcessor.process(reader);
		}

		assertTrue("no lines to console: " + defaultLineProcessor.lines(), defaultLineProcessor.lines().isEmpty());
		ImmutableList<LogEntry> entries = streamListener.entries();

		assertEquals("Log Lines", 1, entries.size());

		LogEntry logEntry = entries.get(0);

		AbstractStackFrame rootCause = logEntry.stackTrace().get().rootCause();
		assertEquals("rootCause", "java.lang.NullPointerException", rootCause.exception().exceptionClass());

		At rootCauseAt = rootCause.firstAt().get();
		assertEquals("firstRootCause", "de.flapdoodle.stuff.commons.lang.BrowserWrapper", rootCauseAt.classname());
		assertEquals("firstRootCause", "isPompey", rootCauseAt.method());

		CauseBy causeBy = logEntry.stackTrace().get().cause().get();

		System.out.println(logEntry.stackTrace().get().toString());

		More more = causeBy.firstStackLines().more().get();
		assertEquals("moreLines", 64, more.count());
	}
}
