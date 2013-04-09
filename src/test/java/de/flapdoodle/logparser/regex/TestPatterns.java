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
package de.flapdoodle.logparser.regex;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertTrue;

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

		Assert.assertTrue("Match", match.isPresent());
		Assert.assertEquals("firstname", "Susi", match.get().get("firstname"));
		Assert.assertEquals("secondname", "Sauerbraten", match.get().get("secondname"));
	}

	@Test
	public void joinPatterns() {
		Pattern name = Patterns.build("[a-zA-Z\\W]+");
		Pattern firstname = Patterns.namedGroup("firstname", name);
		Pattern secondname = Patterns.namedGroup("secondname", name);

		Pattern start = Patterns.build("^");
		Pattern end = Patterns.build("$");
		Pattern space = Patterns.build("\\s+");

		Pattern joinedPattern = Patterns.join(start, firstname, space, secondname, end);

		Assert.assertEquals("^(?<firstname>[a-zA-Z\\W]+)\\s+(?<secondname>[a-zA-Z\\W]+)$", joinedPattern.pattern());

		String line = "Hans Müller";

		Assert.assertTrue(line, Patterns.find(joinedPattern, line));
	}

	@Test
	public void groupNamesAreTheSame() {
		Patterns.IPatternNameSetExtractor extractorA = new Patterns.ProtectedMethodCallSetExtractor();
		Patterns.IPatternNameSetExtractor extractorB = new Patterns.ParsePatternForGroupNamesNameSetExtractor();

		assertSameGroupNames("^(?<firstname>[a-zA-Z\\W]+)\\s+(?<secondname>[a-zA-Z\\W]+)$", extractorA, extractorB);
		assertSameGroupNames("(?<start1>[a-z]+)(?<end>[0-9]+)", extractorA, extractorB);
	}

	private void assertSameGroupNames(String regex, Patterns.IPatternNameSetExtractor extractorA,
			Patterns.IPatternNameSetExtractor extractorB) {
		Pattern pattern = Pattern.compile(regex);
		Set<String> groupNamesNative = extractorA.names(pattern);
		Set<String> groupNamesFallback = extractorB.names(pattern);

		Assert.assertEquals("" + regex, ImmutableSet.of(),
				Sets.difference(groupNamesNative, groupNamesFallback).immutableCopy());
	}

	@Test
	public void performanceTests() {

		String regexWithGroups = "(?<date>\\d+-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) (?<level>[A-Z]+) [a-z]+-[a-z]+-\\d+ \\[\\d+\\] \\[\\d+/[A-Z0-9]+-[a-z]\\d.[a-z]+\\d+[a-z]/\\d+\\] (?<class>([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*):(?<lineNr>\\d+): (?<message>.*)$";
		String regexWithoutGroups = "(\\d+-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) ([A-Z]+) [a-z]+-[a-z]+-\\d+ \\[\\d+\\] \\[\\d+/[A-Z0-9]+-[a-z]\\d.[a-z]+\\d+[a-z]/\\d+\\] (([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*):(\\d+): (.*)$";
		String curRegex = "^(?<date>((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9]),\\d\\d\\d)"
				+ " (?<level>INFO|WARN|ERROR)"
				+ " ([0-9a-zA-Z\\-]+)"
				+ " \\[\\d*\\] "
				+ "\\[\\d*/(?<session>(([0-9A-F]+)-n[0-9].bs\\d\\d[a|b])*)/-*[a-f0-9]*\\] "
				+ "(?<class>([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*)" + ":" + "(?<line>\\d+)" + ":" + " (?<message>.*)$";

		List<TestSet> testSets = Lists.newArrayList(new TestSet("groups   ", regexWithGroups), new TestSet("no groups",
				regexWithoutGroups), new TestSet("current", curRegex));

		String line = "2013-04-07 00:00:01,857 ERROR fuu-plu-123 [4321] [87654321/DEADCAFEDEADCAFEDEADCAFEDEADCAFE-n2.bs12b/4321432198] f.x.u.k.l.s.d.StairwayToHeaven:131: We are all happy people\n";

		int max = 10000;

		for (TestSet ts : testSets) {
			Pattern p = Pattern.compile(ts.regex);
			assertTrue(p.matcher(line).find());

			long timeUsed = hamsterRun(max, p, line);
			System.out.println("TimeUsed(" + ts.name + "): " + timeUsed);
		}
	}

	static class TestSet {

		private final String name;
		private final String regex;

		TestSet(String name, String regex) {
			this.name = name;
			this.regex = regex;
		}
	}

	private long hamsterRun(int max, Pattern pattern, String line) {
		long timeUsed = 0;

		Set<String> names = Patterns.names(pattern);

		for (int i = 0; i < max; i++) {
			long start = System.currentTimeMillis();
			assertTrue(Patterns.match(pattern.matcher(line), names).isPresent());
			timeUsed = timeUsed + (System.currentTimeMillis() - start);
		}
		return timeUsed;
	}
}
