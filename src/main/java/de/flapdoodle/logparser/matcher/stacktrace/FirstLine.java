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
package de.flapdoodle.logparser.matcher.stacktrace;

import com.google.common.base.Optional;
import de.flapdoodle.logparser.regex.Patterns;

import java.util.Map;
import java.util.regex.Pattern;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Classname;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

public class FirstLine extends AbstractStackElement {

	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";

	static final Pattern PATTERN = join(namedGroup(EXCEPTION, Classname), compile(": "),
			namedGroup(MESSAGE, compile(".*$")));

	protected FirstLine(String line, Map<String, String> map) {
		super(line, map);
	}

	public String exception() {
		return attribute(EXCEPTION);
	}

	public String message() {
		return attribute(MESSAGE);
	}

	public static boolean find(CharSequence input) {
		return Patterns.find(PATTERN, input);
	}

	public static Optional<FirstLine> match(CharSequence input) {
		return match(input, PATTERN, new IStackElementFactory<FirstLine>() {

			@Override
			public FirstLine newInstance(String line, Map<String, String> attributes) {
				return new FirstLine(line, attributes);
			}
		});
	}

}
