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

import java.util.regex.Pattern;

import de.flapdoodle.logparser.matcher.CustomPatterns;
import de.flapdoodle.logparser.matcher.generic.GenericLogMatcher;
import de.flapdoodle.logparser.regex.Patterns;

public class StandardJavaLoggingMatcher extends GenericLogMatcher {

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

	public StandardJavaLoggingMatcher() {
		super(_firstLine, _secondLine);
	}
}
