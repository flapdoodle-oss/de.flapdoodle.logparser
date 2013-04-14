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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.ILineProcessor;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IRewindableReader;
import de.flapdoodle.logparser.IStreamListener;
import de.flapdoodle.logparser.LogEntry;
import de.flapdoodle.logparser.StreamProcessor;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.WriteToListLineProcessor;
import de.flapdoodle.logparser.matcher.generic.GenericLogMatcher;
import de.flapdoodle.logparser.streamlistener.CollectingStreamListener;

public class TestExampleReadMeCode {

	// ### Usage
	/*
	// ->
	With this library it should be easy to setup a parser for most common and custom log files
	The following examples will show how you can do this.
	// <-
	 */
	
	// #### Simple LogFile
	/*
	// ->
	This is how you can parse a simple logfile (with stacktrace support). In this example the logfile can look
	like this:
	
	
	// @resource readme-sample-simple.txt
	 
	
	// <-
	*/
	
	@Test
	public void testStandard() throws UnknownHostException, IOException {
		CollectingStreamListener<LogEntry> collectingStreamListener = new CollectingStreamListener<LogEntry>();
		WriteToListLineProcessor writeToListLineProcessor = new WriteToListLineProcessor();
		
		try (InputStream inputStream = getClass().getResourceAsStream("readme-sample-simple.txt")) {

		// ->
		// ...
		String regex = "(?<date>\\d+-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d) (?<level>[A-Z]+): (?<message>.*)$";
		Pattern firstLinePattern = Pattern.compile(regex);
		GenericLogMatcher genericLogMatcher = new GenericLogMatcher(firstLinePattern);

		List<IMatcher<LogEntry>> matchers = Lists.<IMatcher<LogEntry>> newArrayList(genericLogMatcher);

		IStreamListener<LogEntry> streamListener = collectingStreamListener;
		ILineProcessor lineProcessor = writeToListLineProcessor;

		StreamProcessor<LogEntry> streamProcessor = new StreamProcessor<LogEntry>(matchers, lineProcessor, streamListener);
		// ...
		// <-

		// ->
		// ...
		int readAheadLimit = 1024;
		IRewindableReader reader = new BufferedReaderAdapter(inputStream, Charsets.UTF_8, readAheadLimit);
		streamProcessor.process(reader);
		// ...
		// <-
		}
		
		assertEquals(2,collectingStreamListener.entries().size());
		assertTrue(writeToListLineProcessor.lines().isEmpty());
	}
}
