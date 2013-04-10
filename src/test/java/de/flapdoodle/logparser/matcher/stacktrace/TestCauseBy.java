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
package de.flapdoodle.logparser.matcher.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.regex.Matcher;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.flapdoodle.logparser.regex.Patterns;

public class TestCauseBy {

	@Test
	public void causeWithSecondCause() {
		Optional<CauseBy> match = CauseBy.match("Caused by: java.lang.IllegalArgumentException: java.lang.NullPointerException: without any cause");
		assertTrue(match.isPresent());

		ImmutableMap<String, String> attributes = match.get().attributes();

		assertEquals("exception", "java.lang.IllegalArgumentException", attributes.get("exception"));
		assertEquals("message", "java.lang.NullPointerException: without any cause", attributes.get("message"));
	}
}
