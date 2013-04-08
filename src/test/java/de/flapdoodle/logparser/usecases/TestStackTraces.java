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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.GenericStreamProcessor;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IRewindableReader;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.Streams;
import de.flapdoodle.logparser.io.WriteToConsoleLineProcessor;
import de.flapdoodle.logparser.matcher.stacktrace.StackTraceMatcher;
import de.flapdoodle.logparser.stacktrace.AbstractStackFrame;
import de.flapdoodle.logparser.stacktrace.At;
import de.flapdoodle.logparser.stacktrace.StackTrace;
import de.flapdoodle.logparser.streamlistener.OnceAndOnlyOnceStreamListener;

public class TestStackTraces {

	@Test
	public void readStacktraceWithCauses() throws IOException {
		
		OnceAndOnlyOnceStreamListener<StackTrace> stackListener = new OnceAndOnlyOnceStreamListener<StackTrace>();
		
		try (InputStream stream = getClass().getResourceAsStream("stacktrace-with-causes.txt")) {
			IRewindableReader reader = new BufferedReaderAdapter(stream, Charsets.UTF_8, 1024);
			GenericStreamProcessor<StackTrace> streamProcessor = new GenericStreamProcessor<StackTrace>(
					Lists.<IMatcher<StackTrace>> newArrayList(new StackTraceMatcher()), new WriteToConsoleLineProcessor(),
					stackListener);

			streamProcessor.process(reader);
		}
		
		Optional<StackTrace> chanceOfStackTrace = stackListener.value();
		assertTrue(chanceOfStackTrace.isPresent());
		StackTrace stackTrace=chanceOfStackTrace.get();
		
		assertNotNull(stackTrace);
		assertNotNull(stackTrace.cause().isPresent());
		assertNotNull(stackTrace.cause().get().cause().isPresent());
		
		assertEquals("exceptionClass","java.lang.RuntimeException",stackTrace.exception().exceptionClass());
		assertEquals("exceptionMessage","middle",stackTrace.exception().message());
		
		AbstractStackFrame rootCause = stackTrace.rootCause();
		assertNotNull("rootCause",rootCause);
		assertTrue(rootCause.firstAt().isPresent());
		At at = rootCause.firstAt().get();
		assertEquals("rootCause.at","de.flapdoodle.logparser.usecases.TestJavaLogging",at.classname());
		assertEquals("rootCause.at","inner",at.method());
	}

}
