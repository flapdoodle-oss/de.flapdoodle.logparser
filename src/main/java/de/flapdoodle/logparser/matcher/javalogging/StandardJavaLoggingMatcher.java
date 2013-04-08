/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.flapdoodle.logparser.GenericStreamProcessor;
import de.flapdoodle.logparser.ILineProcessor;
import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.IStreamListener;
import de.flapdoodle.logparser.LogEntry;
import de.flapdoodle.logparser.io.StringListReaderAdapter;
import de.flapdoodle.logparser.matcher.CustomPatterns;
import de.flapdoodle.logparser.matcher.stacktrace.StackTraceMatcher;
import de.flapdoodle.logparser.regex.Patterns;
import de.flapdoodle.logparser.stacktrace.AbstractStackFrame;
import de.flapdoodle.logparser.stacktrace.StackTrace;

public class StandardJavaLoggingMatcher implements IMatcher<LogEntry> {

	private static final String P_DATE = "date";
	private static final String P_CLASS = "class";
	private static final String P_METHOD = "method";
	private static final String P_LEVEL = "level";
	private static final String P_MESSAGE = "message";

	static Pattern _firstLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup(P_DATE, CustomPatterns.Date),
			Patterns.build("(\\s+)"), Patterns.namedGroup(P_CLASS, CustomPatterns.Classname), Patterns.build("(\\s+)"),
			Patterns.namedGroup(P_METHOD, CustomPatterns.Method));

	static Pattern _secondLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup(P_LEVEL, CustomPatterns.Levels),
			Patterns.namedGroup(P_MESSAGE, Patterns.build(".*")), Patterns.build("$"));

	@Override
	public Optional<IMatch<LogEntry>> match(IReader reader) throws IOException {
		//		int currentPosition = 0;

		Optional<String> possibleFirstLine = reader.nextLine();
		if (possibleFirstLine.isPresent()) {
			String firstLine=possibleFirstLine.get();
			Optional<Map<String, String>> match = Patterns.match(_firstLine.matcher(firstLine));
			if (match.isPresent()) {
				Optional<String> possibleSecondLine = reader.nextLine();
				if (possibleSecondLine.isPresent()) {
					String secondLine=possibleSecondLine.get();
					Optional<Map<String, String>> secondMatch = Patterns.match(_secondLine.matcher(secondLine));
					if (secondMatch.isPresent()) {
						return Optional.<IMatch<LogEntry>> of(new FullLineMatcher(firstLine,match.get(),secondLine,secondMatch.get()));
					}
				}
			}
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

	static class FullLineMatcher implements IMatch<LogEntry> {

		private final String _firstLine;
		private final Map<String, String> _firstMatch;
		private final String _secondLine;
		private final Map<String, String> _secondMatch;

		public FullLineMatcher(String firstLine, Map<String, String> firstMatch, String secondLine, Map<String,String> secondMatch) {
			_firstLine = firstLine;
			_firstMatch = firstMatch;
			_secondLine = secondLine;
			_secondMatch = secondMatch;
		}

		@Override
		public LogEntry process(List<String> lines) throws IOException {
			System.out.println(_firstLine);
			System.out.println("--" + _firstMatch);

			System.out.println(_secondLine);
			System.out.println("--" + _secondMatch);
			
			if (!lines.isEmpty()) {

//				String secondLine = lines.get(0);
//				Optional<Map<String, String>> secondMatch = Patterns.match(_secondLine.matcher(secondLine));

				GenericStreamProcessor<StackTrace> contentProcessor = new GenericStreamProcessor<StackTrace>(Lists.<IMatcher<StackTrace>>newArrayList(new StackTraceMatcher()), new ILineProcessor() {
					
					@Override
					public void processLine(String line) {
						System.out.println(">>" + line);
					}
				},new IStreamListener<StackTrace>() {
					public void entry(StackTrace value) {
						System.out.println("Entry:[" + value+"]");
					};
				});
				
				contentProcessor.process(new StringListReaderAdapter(lines));

//				List<String> content = lines;

//				Optional<StackTrace> stacktrace = StackTraceParser.parse(content);
//				if (stacktrace.isPresent()) {
//					System.out.println(">> stacktrace found: " + stacktrace.get());
//				} else {
//
//					for (String line : content) {
//						System.out.println(">> " + line);
//					}
//				}
				
			}
			
			return new LogEntry(_firstMatch,_secondMatch);
		}
	}
	
}
