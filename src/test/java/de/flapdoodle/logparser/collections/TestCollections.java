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
package de.flapdoodle.logparser.collections;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


public class TestCollections {

	@Test
	public void joinWithoutCollision() {
		Map<String, String> a=ImmutableMap.<String, String>builder().put("a", "a").put("a1", "a1").build();
		Map<String, String> b=ImmutableMap.<String, String>builder().put("b", "b").put("b1", "b1").build();
		
		Map<String, String> joined = Collections.join(a, b);
		
		assertEquals("Keys",4,joined.size());
		assertEquals("Keys","a",joined.get("a"));
		assertEquals("Keys","a1",joined.get("a1"));
		assertEquals("Keys","b",joined.get("b"));
		assertEquals("Keys","b1",joined.get("b1"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void joinWithCollision() {
		Map<String, String> a=ImmutableMap.<String, String>builder().put("a", "a").put("c", "c1").build();
		Map<String, String> b=ImmutableMap.<String, String>builder().put("b", "b").put("c", "c2").build();
		
		Map<String, String> joined = Collections.join(a, b);
	}
	
	@Test
	public void join3WithoutCollision() {
		Map<String, String> a=ImmutableMap.<String, String>builder().put("a", "a").put("a1", "a1").build();
		Map<String, String> b=ImmutableMap.<String, String>builder().put("b", "b").put("b1", "b1").build();
		Map<String, String> c=ImmutableMap.<String, String>builder().put("c", "c").put("c1", "c1").build();
		
		Map<String, String> joined = Collections.join(a, b, c);
		
		assertEquals("Keys",6,joined.size());
		assertEquals("Keys","a",joined.get("a"));
		assertEquals("Keys","a1",joined.get("a1"));
		assertEquals("Keys","b",joined.get("b"));
		assertEquals("Keys","b1",joined.get("b1"));
		assertEquals("Keys","c",joined.get("c"));
		assertEquals("Keys","c1",joined.get("c1"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void join3WithCollision() {
		Map<String, String> a=ImmutableMap.<String, String>builder().put("a", "a").put("c", "c1").build();
		Map<String, String> b=ImmutableMap.<String, String>builder().put("b", "b").put("d", "d1").build();
		Map<String, String> c=ImmutableMap.<String, String>builder().put("c", "c2").put("d", "d2").build();
		
		Map<String, String> joined = Collections.join(a, b, c);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void joinNWithCollision() {
		List<Map<String,String>> maps=Lists.newArrayList();
		for (String  s : Lists.newArrayList("a","b","c","d","e")) {
			maps.add(ImmutableMap.<String, String>builder().put(s,s).build());
		}
		maps.add(ImmutableMap.<String, String>builder().put("a","A").build());
		
		Map<String, String> joined = Collections.join(maps);
	}
	
}
