package de.flapdoodle.logparser.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.StreamProcessor;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.Streams;
import de.flapdoodle.logparser.io.WriteToConsoleLineProcessor;
import de.flapdoodle.logparser.matcher.javalogging.StandardJavaLoggingMatcher;


public class TestJavaLogging {

	
	@Test
	public void readLogfile() throws IOException {
		try (InputStream stream = Streams.compressed(getClass().getResourceAsStream("java-logging-stacktrace-sample.txt.gz"))) {
			IReader reader=new BufferedReaderAdapter(stream, Charsets.UTF_8,1024);
			StreamProcessor streamProcessor=new StreamProcessor(Lists.<IMatcher>newArrayList(new StandardJavaLoggingMatcher()), new WriteToConsoleLineProcessor());
			
			streamProcessor.process(reader);
		}
	}
	
//	@Test
	public void createStacktraceWithThreeCauses() {
		
		Logger logger=Logger.getLogger(getClass().getName());
		
		try {
			logger.info("some info");
			logger.warning("some error\nwith a second line");
			logger.severe("some error");
			outer();
		} catch (Exception ex) {
			logger.log(Level.WARNING,"outer",ex);
		}
	}
	
//	@Test
	public void createStacktraceWithOneCauses() {
		
		Logger logger=Logger.getLogger(getClass().getName());
		
		try {
			logger.info("some info");
			logger.warning("some error\nwith a second line");
			logger.severe("some error");
			inner();
		} catch (Exception ex) {
			logger.log(Level.WARNING,"inner",ex);
		}
	}
	
	void outer() {
		try {
			middle();
		} catch(IllegalArgumentException iax) {
			throw new RuntimeException("middle",iax);
		}
	}
	
	void middle() {
		try {
			inner();
		} catch (NullPointerException npx) {
			throw new IllegalArgumentException(npx);
		}
	}
	
	void inner() {
		throw new NullPointerException("without any cause");
	}
}
