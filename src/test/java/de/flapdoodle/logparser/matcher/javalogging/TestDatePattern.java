package de.flapdoodle.logparser.matcher.javalogging;

import java.util.regex.Matcher;

import org.junit.Assert;
import org.junit.Test;

public class TestDatePattern {

	@Test
	public void validLine() {
		String datePart = "Apr 04, 2013 8:15:44 PM";
		
		DatePattern pattern = new DatePattern();
		Matcher matcher = pattern.matcher(datePart);
		Assert.assertTrue(matcher.find());
		Assert.assertEquals(datePart, matcher.group());
	}

	@Test
	public void wrongTime() {
		String datePart = "Apr 04, 2013 8:65:44 PM";
		
		DatePattern pattern = new DatePattern();
		Matcher matcher = pattern.matcher(datePart);
		Assert.assertFalse(matcher.find());
	}
}
