/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *   ${lic.username2} <${lic.email2}>
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
package de.flapdoodle.logparser.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedPattern {

	private final String _name;
	private final Pattern _pattern;

	public NamedPattern(String pattern) {
		this(null,pattern);
	}
	
	public NamedPattern(String name, String pattern) {
		_name = name;
		_pattern = Pattern.compile(pattern);
	}

	public Matcher matcher(String input) {
		return _pattern.matcher(input);
	}

	public String name() {
		return _name;
	}

	public String patternAsString() {
		return _pattern.pattern();
	}
}
