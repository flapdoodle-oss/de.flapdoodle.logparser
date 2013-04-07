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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.stacktrace.At;
import de.flapdoodle.logparser.stacktrace.Cause;
import de.flapdoodle.logparser.stacktrace.ExceptionAndMessage;
import de.flapdoodle.logparser.stacktrace.More;
import de.flapdoodle.logparser.stacktrace.StackTrace;




public final class StackTraceParser {

	private static final String COUNT_GROUP_ID = "COUNT";
	private static final String METHOD_GROUP_ID = "METHOD";
	private static final String FILE_GROUP_ID = "FILE";
	private static final String MESSAGE_GROUP_ID = "MESSAGE";
	private static final String LINE_GROUP_ID = "LINE";
	private static final String CLASSNAME_GROUP_ID = "CLASSNAME";

	private static final String CLASSNAME_PATTERN = "(?<" + CLASSNAME_GROUP_ID + ">([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*)";

	static final Pattern AT_LINE_PATTERN = Pattern.compile("^(\\s+)at(\\s+)" + CLASSNAME_PATTERN + "\\.(?<"
			+ METHOD_GROUP_ID + ">([a-zA-Z][a-zA-Z0-9]*))" + "\\(((Native Method)|((?<" + FILE_GROUP_ID
			+ ">([a-zA-Z]+)\\.java):(?<" + LINE_GROUP_ID + ">\\d+)))\\)$");

	static final Pattern EXCEPTION_PATTERN = Pattern.compile(CLASSNAME_PATTERN + ":(?<" + MESSAGE_GROUP_ID + ">.*)$");

	static final Pattern CAUSE_BY_PATTERN = Pattern.compile("Caused(\\s+)by:(\\s+)" + CLASSNAME_PATTERN + ":(?<"
			+ MESSAGE_GROUP_ID + ">.*)$");

	static final Pattern MORE_PATTERN = Pattern.compile("(\\s+)\\.\\.\\.(\\s+)(?<" + COUNT_GROUP_ID + ">\\d+) more");
	
	private StackTraceParser() {
		// no instance
	}
	
	public static Optional<StackTrace> parse(List<String> stacktraceLines) {
		if (stacktraceLines.size() > 2) {
			Matcher matcher = AT_LINE_PATTERN.matcher(stacktraceLines.get(1));
			if (matcher.find()) {
				Matcher exmatcher = EXCEPTION_PATTERN.matcher(stacktraceLines.get(0));
				if (exmatcher.find()) {
					ExceptionAndMessage outerException = new ExceptionAndMessage(classname(exmatcher), message(exmatcher));

					List<At> stack = Lists.newArrayList();
					Cause cause=null;

					for (int i = 1, s = stacktraceLines.size(); i < s; i++) {
						String line = stacktraceLines.get(i);

						Matcher atMatcher = AT_LINE_PATTERN.matcher(line);
						if (atMatcher.find()) {
							stack.add(new At(classname(atMatcher), method(atMatcher), file(atMatcher), line(atMatcher)));
						} else {
							Matcher causeMatcher = CAUSE_BY_PATTERN.matcher(line);
							if (causeMatcher.find()) {
								cause=getCause(causeMatcher, stacktraceLines.subList(i+1, stacktraceLines.size()));
								i=s;
							}
						}
					}
					
					return Optional.of(new StackTrace(stacktraceLines,outerException,stack,cause));

				} else {
					System.out.println("FirstLine NOT Exception: " + stacktraceLines.get(0));
				}
			}
		}
		return Optional.absent();
	}

	private static Cause getCause(Matcher match, List<String> left) {
		List<At> stack = Lists.newArrayList();
		Cause cause=null;
		More more=null;
		
		for (int i = 0, s = left.size(); i < s; i++) {
			String line = left.get(i);

			Matcher atMatcher = AT_LINE_PATTERN.matcher(line);
			if (atMatcher.find()) {
				stack.add(new At(classname(atMatcher), method(atMatcher), file(atMatcher), line(atMatcher)));
			} else {
				Matcher causeMatcher = CAUSE_BY_PATTERN.matcher(line);
				if (causeMatcher.find()) {
					cause=getCause(causeMatcher, left.subList(i+1, left.size()));
					i=s;
				} else {
					Matcher moreMatcher = MORE_PATTERN.matcher(line);
					if (moreMatcher.find()) {
						more=new More(count(moreMatcher));
					}
				}
			}
		}
		
		return new Cause(new ExceptionAndMessage(classname(match), message(match)),stack,cause,more);
	}

	private static String count(Matcher moreMatcher) {
		return moreMatcher.group(COUNT_GROUP_ID);
	}

	private static String line(Matcher atMatcher) {
		return atMatcher.group(LINE_GROUP_ID);
	}

	private static String file(Matcher atMatcher) {
		return atMatcher.group(FILE_GROUP_ID);
	}

	private static String method(Matcher atMatcher) {
		return atMatcher.group(METHOD_GROUP_ID);
	}

	private static String message(Matcher exmatcher) {
		return exmatcher.group(MESSAGE_GROUP_ID);
	}

	private static String classname(Matcher exmatcher) {
		return exmatcher.group(CLASSNAME_GROUP_ID);
	}


}
