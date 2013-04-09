/**
 * Copyright (C) 2013
 * Michael Mosmann <michael@mosmann.de>
 * 
 * with contributions from
 * ${lic.developers}
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.junit.Test;

import com.google.common.base.Optional;

public class TestMore {

	@Test
	public void numberWithTwoDigits() {
		Optional<More> match = More.match("\t... 25 more");
		assertTrue(match.isPresent());
		assertEquals("count", "25", match.get().attributes().get("count"));
	}

}
