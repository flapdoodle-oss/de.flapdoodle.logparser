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
package de.flapdoodle.logparser.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TestCustomPatterns {

	@Test
	public void className() {
		assertMatch(CustomPatterns.Classname, "de.haha.Huhu");
		assertNotFullMatch(CustomPatterns.Classname, "de.haha.");
		assertMatch(CustomPatterns.Classname, "org.junit.runners.BlockJUnit4ClassRunner");
	}

	//	@Test
	//	public void packages() {
	//		assertMatch(CustomPatterns.Packages,"de.haha.a.b.c");
	//		assertNotFullMatch(CustomPatterns.Packages,"de.haha.A.b.c");
	//	}

	private void assertMatch(Pattern pattern, String toMatch) {
		Assert.assertTrue("Pattern: " + pattern + "<->" + toMatch, pattern.matcher(toMatch).find());
	}

	private void assertNoMatch(Pattern pattern, String notToMatch) {
		Matcher matcher = pattern.matcher(notToMatch);
		if (matcher.find()) {
			Assert.assertEquals("Pattern: " + pattern + "<->" + notToMatch, "", matcher.group());
		}
	}

	private void assertNotFullMatch(Pattern pattern, String notToMatch) {
		Matcher matcher = pattern.matcher(notToMatch);
		Assert.assertTrue("Pattern: " + pattern + "<->" + notToMatch, matcher.find());
		String match = matcher.group();
		Assert.assertFalse(notToMatch + "!=" + match, notToMatch.equals(match));
	}

	@Test
	public void validDate() {
		String datePart = "Apr 04, 2013 8:15:44 PM";

		Matcher matcher = CustomPatterns.Date.matcher(datePart);
		Assert.assertTrue(matcher.find());
		Assert.assertEquals(datePart, matcher.group());
	}

	@Test
	public void wrongTime() {
		String datePart = "Apr 04, 2013 8:65:44 PM";

		Matcher matcher = CustomPatterns.Date.matcher(datePart);
		Assert.assertFalse(matcher.find());
	}

}
