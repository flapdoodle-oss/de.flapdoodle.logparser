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
package de.flapdoodle.logparser.matcher.javalogging;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.matcher.CustomPatterns;
import de.flapdoodle.logparser.regex.Patterns;
import de.flapdoodle.logparser.stacktrace.StackTrace;

public class StandardJavaLoggingMatcher implements IMatcher {

	//	NamedPatterns _firstLine = new NamedPatterns(new NamedPattern("^"), new DatePattern(), new NamedPattern("(\\s+)"),
	//			new ClassPattern(), new NamedPattern("(\\s+)"), new MethodPattern());

	static Pattern _firstLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup("date", CustomPatterns.Date),
			Patterns.build("(\\s+)"), Patterns.namedGroup("class", CustomPatterns.Classname), Patterns.build("(\\s+)"),
			Patterns.namedGroup("method", CustomPatterns.Method));

	//	NamedPattern _levelPattern = new LevelPattern();
	//	NamedPatterns _secondLine = new NamedPatterns(new NamedPattern("^"), new LevelPattern(), new NamedPattern("message",
	//			"(?<message>.*)$"));

	static Pattern _secondLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup("level", CustomPatterns.Levels),
			Patterns.namedGroup("message", Patterns.build(".*")), Patterns.build("$"));

	@Override
	public Optional<IMatch> match(String firstLine) {
		//		int currentPosition = 0;

		Optional<Map<String, String>> match = Patterns.match(_firstLine.matcher(firstLine));
		if (match.isPresent()) {
			return Optional.<IMatch> of(new FullLineMatcher(firstLine,match));
		}
		return Optional.absent();
		//		for (NamedPattern p : _patterns) {
		//			Matcher matcher = p.matcher(firstLine);
		//			if (matcher.find(currentPosition)) {
		////				System.out.println(p.name() + "=" + matcher.group());
		//				currentPosition = matcher.end();
		//			} else {
		//				return false;
		//			}
		//		}
		//		if (firstLine.length()>currentPosition) {
		//			System.out.println("left=" + firstLine.substring(currentPosition));
		//		}
	}

	static class FullLineMatcher implements IMatch {

		private final String _firstLine;
		private final Optional<Map<String, String>> _match;

		public FullLineMatcher(String firstLine, Optional<Map<String, String>> match) {
			_firstLine = firstLine;
			_match = match;
		}

		@Override
		public void process(List<String> lines) throws IOException {
			if (!lines.isEmpty()) {

				String secondLine = lines.get(0);
				Optional<Map<String, String>> secondMatch = Patterns.match(_secondLine.matcher(secondLine));

				System.out.println(_firstLine);
				System.out.println("--" + _match.get());

				System.out.println(secondLine);
				System.out.println("--" + secondMatch.get());

				List<String> content = lines.subList(1, lines.size());

				Optional<StackTrace> stacktrace = StackTraceParser.parse(content);
				if (stacktrace.isPresent()) {
					System.out.println(">> stacktrace found: " + stacktrace.get());
				} else {

					for (String line : content) {
						System.out.println(">> " + line);
					}
				}
			}
		}
	}
}
