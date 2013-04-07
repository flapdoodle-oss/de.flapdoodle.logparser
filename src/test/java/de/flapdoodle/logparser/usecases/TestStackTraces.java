package de.flapdoodle.logparser.usecases;

import java.io.IOException;
import java.io.InputStream;

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
import de.flapdoodle.logparser.matcher.javalogging.StackTraceMatcher;


public class TestStackTraces {

	@Test
	public void readStacktraceWithCauses() throws IOException {
		try (InputStream stream = getClass().getResourceAsStream("stacktrace-with-causes.txt")) {
			IRewindableReader reader=new BufferedReaderAdapter(stream, Charsets.UTF_8,1024);
			GenericStreamProcessor<String> streamProcessor=new GenericStreamProcessor<String>(Lists.<IMatcher<String>>newArrayList(new StackTraceMatcher()), new WriteToConsoleLineProcessor(),new IStreamListener<String>() {
				@Override
				public void entry(String value) {
					System.out.println("Entry: "+value);
				}
			});
			
			streamProcessor.process(reader);
		}
	}

}
