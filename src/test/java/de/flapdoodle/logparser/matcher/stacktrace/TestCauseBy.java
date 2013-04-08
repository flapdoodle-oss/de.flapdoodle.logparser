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
		
		assertEquals("exception", "java.lang.IllegalArgumentException",attributes.get("exception"));
		assertEquals("message", "java.lang.NullPointerException: without any cause",attributes.get("message"));
	}
}
