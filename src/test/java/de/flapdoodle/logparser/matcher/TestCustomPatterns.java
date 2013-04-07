package de.flapdoodle.logparser.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;


public class TestCustomPatterns {

	@Test
	public void className() {
		assertMatch(CustomPatterns.Classname,"de.haha.Huhu");
		assertNoMatch(CustomPatterns.Classname,"de.haha.");
	}
	
//	@Test
//	public void packages() {
//		assertMatch(CustomPatterns.Packages,"de.haha.a.b.c");
//		assertNotFullMatch(CustomPatterns.Packages,"de.haha.A.b.c");
//	}

	private void assertMatch(Pattern pattern, String toMatch) {
		Assert.assertTrue("Pattern: "+pattern+"<->"+toMatch,pattern.matcher(toMatch).find());
	}
	private void assertNoMatch(Pattern pattern, String notToMatch) {
		Matcher matcher = pattern.matcher(notToMatch);
		if (matcher.find()) {
			Assert.assertEquals("Pattern: "+pattern+"<->"+notToMatch,"",matcher.group());
		}
	}
	private void assertNotFullMatch(Pattern pattern, String notToMatch) {
		Matcher matcher = pattern.matcher(notToMatch);
		Assert.assertTrue("Pattern: "+pattern+"<->"+notToMatch,matcher.find());
		String match = matcher.group();
		Assert.assertFalse(notToMatch+"!="+match,notToMatch.equals(match));
	}
}
