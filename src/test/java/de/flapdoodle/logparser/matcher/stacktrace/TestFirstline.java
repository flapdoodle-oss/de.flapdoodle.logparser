package de.flapdoodle.logparser.matcher.stacktrace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;


public class TestFirstline {
	@Test
	public void exceptionAndMessage() {
		Optional<FirstLine> match = FirstLine.match("java.lang.RuntimeException: middle");
		assertTrue(match.isPresent());
		
		ImmutableMap<String, String> attributes = match.get().attributes();
		
		assertEquals("exception", "java.lang.RuntimeException",attributes.get("exception"));
		assertEquals("message", "middle",attributes.get("message"));
	}

}
