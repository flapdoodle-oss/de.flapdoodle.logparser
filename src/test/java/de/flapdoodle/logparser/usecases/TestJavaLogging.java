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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.GenericStreamProcessor;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.IRewindableReader;
import de.flapdoodle.logparser.io.BufferedReaderAdapter;
import de.flapdoodle.logparser.io.Streams;
import de.flapdoodle.logparser.io.WriteToConsoleLineProcessor;
import de.flapdoodle.logparser.matcher.javalogging.StandardJavaLoggingMatcher;


public class TestJavaLogging {

	
	@Test
	public void readLogfile() throws IOException {
		try (InputStream stream = Streams.compressed(getClass().getResourceAsStream("java-logging-stacktrace-sample.txt.gz"))) {
			IRewindableReader reader=new BufferedReaderAdapter(stream, Charsets.UTF_8,1024);
			GenericStreamProcessor streamProcessor=new GenericStreamProcessor(Lists.<IMatcher>newArrayList(new StandardJavaLoggingMatcher()), new WriteToConsoleLineProcessor());
			
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
