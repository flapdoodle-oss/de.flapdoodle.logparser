package de.flapdoodle.logparser.usecases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.GenericStreamProcessor;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IRewindableReader;
import de.flapdoodle.logparser.IStreamListener;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.Streams;
import de.flapdoodle.logparser.io.WriteToConsoleLineProcessor;
import de.flapdoodle.logparser.matcher.stacktrace.StackTraceMatcher;
import de.flapdoodle.logparser.stacktrace.AbstractStackFrame;
import de.flapdoodle.logparser.stacktrace.At;
import de.flapdoodle.logparser.stacktrace.StackTrace;

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
		
		StackTrace stackTrace = stackListener.value();
		
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
	
	static class OnceAndOnlyOnceStreamListener<T> implements IStreamListener<T> {

		boolean firstCall=true;
		private T _value;

		@Override
		public void entry(T value) {
			if (!firstCall) throw new IllegalArgumentException("called more than once");
			
			firstCall=false;
			_value=value;
		}

		public T value() {
			return _value;
		}
		
	}

}
