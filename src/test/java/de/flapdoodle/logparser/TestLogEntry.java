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

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.stacktrace.StackTrace;


public class TestLogEntry {

	@Test
	public void sampleEntry() {
		List<String> source=Lists.newArrayList("line 1","line 2");
		Map<String, String> attributes=ImmutableMap.<String,String>builder().put("a", "value").build();
		Optional<StackTrace> stackTrace=Optional.absent();
		List<String> messages=Lists.newArrayList("message not in a bottle");
		
		LogEntry entry = new LogEntry(source, attributes, stackTrace, messages);
		
		assertEquals("line 2",entry.source().get(1));
		assertEquals("value",entry.attributes().get("a"));
		assertNull(entry.attributes().get("b"));
		assertEquals("message not in a bottle",entry.messages().get(0));
		assertFalse(entry.stackTrace().isPresent());
	}
}
