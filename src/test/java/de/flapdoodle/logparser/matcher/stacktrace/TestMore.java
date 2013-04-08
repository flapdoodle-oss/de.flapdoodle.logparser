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
		assertEquals("count", "25",match.get().attributes().get("count"));
	}

}
