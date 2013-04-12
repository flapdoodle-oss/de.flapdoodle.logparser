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
package de.flapdoodle.logparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import de.flapdoodle.logparser.io.StringListReaderAdapter;
import de.flapdoodle.logparser.io.WriteToListLineProcessor;
import de.flapdoodle.logparser.streamlistener.CollectingStreamListener;


public class TestStreamProcessor {

	private StreamProcessor<StreamEntry> _processor;
	private CollectingStreamListener<StreamEntry> _listener;
	private WriteToListLineProcessor _lineProcessor;

	@Before
	public void before() {
		CollectingStreamListener<StreamEntry> listener=new CollectingStreamListener<>();
		WriteToListLineProcessor defaultLineProcessor = new WriteToListLineProcessor();
		
		StreamProcessor<StreamEntry> processor = new StreamProcessor<>(Lists.newArrayList(new StreamMatcher()), defaultLineProcessor, listener);
		
		_listener=listener;
		_lineProcessor=defaultLineProcessor;
		_processor=processor;
	}
	
	@Test
	public void emptyStreamWillProduceNoEntryAndNoLine() throws IOException {
		List<String> lines=Lists.newArrayList();
		_processor.process(new StringListReaderAdapter(lines));

		assertTrue(_lineProcessor.lines().isEmpty());
		assertTrue(_listener.entries().isEmpty());
	}
	
	@Test
	public void onlyMessageLines() throws IOException {
		List<String> lines=Lists.newArrayList();
		lines.add("message 1");
		lines.add("message 2");
		lines.add("message 3");
		lines.add("message 4");
		_processor.process(new StringListReaderAdapter(lines));

		assertTrue(_lineProcessor.lines().isEmpty());
		assertEquals(4,_listener.entries().size());
		assertEquals("message 3",_listener.entries().get(2).message());
		
		for (StreamEntry entry : _listener.entries()) {
			assertTrue(entry.comments().isEmpty());
		}
	}

	@Test
	public void messageWithCommentsMixed() throws IOException {
		List<String> lines=Lists.newArrayList();
		lines.add("message 1");
		lines.add("> comment 1");
		lines.add("message 2");
		lines.add("message 3");
		lines.add("> comment 2");
		lines.add("> comment 3");
		lines.add("> comment 4");
		lines.add("message 4");
		lines.add("> comment 5");
		_processor.process(new StringListReaderAdapter(lines));

		assertTrue("no lines",_lineProcessor.lines().isEmpty());
		assertEquals("size",4,_listener.entries().size());
		assertEquals("message 3",_listener.entries().get(2).message());
		
		assertEquals("message 2 has 0 comments",0,_listener.entries().get(1).comments().size());
		
		assertEquals("message 3 has 3 comments",3,_listener.entries().get(2).comments().size());
		assertEquals("> comment 3",_listener.entries().get(2).comments().get(1));
		
		assertEquals("message 4 has 1 comments",1,_listener.entries().get(3).comments().size());
		assertEquals("> comment 5",_listener.entries().get(3).comments().get(0));
	}
}
