package de.flapdoodle.logparser.matcher;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class TestPatterns {

	@Test
	public void patternsWithGroups() {
		Pattern pattern = Patterns.build("(?<start>[a-zA-Z]+):(?<end>[a-zA-Z]+(?<number>[0-9]+))");
		Matcher matcher = pattern.matcher("halli:hallo12");
		
		Optional<Map<String, String>> match = Patterns.match(matcher);
		Assert.assertTrue(match.isPresent());
		Assert.assertEquals("start", "halli", match.get().get("start"));
		Assert.assertEquals("end", "hallo12", match.get().get("end"));
		Assert.assertEquals("number", "12", match.get().get("number"));
	}

	@Test(expected = PatternSyntaxException.class)
	public void groupNameCollision() {
		Pattern pattern = Patterns.build("(?<bla>[a-z]+)", "\\s", "(?<bla>[a-z]+)");
	}

	@Test
	public void groupNames() {
		Pattern pattern = Patterns.build("(?<a>[a-z]+)", "\\s", "(?<b>[a-z]+)");
		Matcher matcher = pattern.matcher("abc def");
		Optional<Map<String, String>> match = Patterns.match(matcher);
		Assert.assertTrue(match.isPresent());
		Assert.assertEquals("a", "abc", match.get().get("a"));
		Assert.assertEquals("b", "def", match.get().get("b"));
	}

	@Test
	public void namedGroup() {
		Pattern wordPattern = Patterns.build("[\\w]+");
		Pattern namePattern = Patterns.join(Patterns.namedGroup("firstname", wordPattern), Patterns.build("\\s"),
				Patterns.namedGroup("secondname", wordPattern));
		
		Matcher matcher = namePattern.matcher("Susi Sauerbraten");
		Optional<Map<String, String>> match = Patterns.match(matcher);
		
		Assert.assertTrue("Match",match.isPresent());
		Assert.assertEquals("firstname", "Susi", match.get().get("firstname"));
		Assert.assertEquals("secondname", "Sauerbraten", match.get().get("secondname"));
	}
}
