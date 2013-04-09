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
package de.flapdoodle.logparser.matcher.stacktrace;

import com.google.common.base.Optional;
import de.flapdoodle.logparser.regex.Patterns;

import java.util.Map;
import java.util.regex.Pattern;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Space;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

public class More extends AbstractStackElement {

	private static final String COUNT = "count";

	private static final Pattern PATTERN = join(Space, compile("\\.\\.\\."), Space, namedGroup(COUNT, compile("\\d+")),
			compile(" (more|common frames omitted)"));

	protected More(String line, Map<String, String> attributes) {
		super(line, attributes);
	}

	public String count() {
		return attribute(COUNT);
	}

	public static boolean find(CharSequence input) {
		return Patterns.find(PATTERN, input);
	}

	public static Optional<More> match(CharSequence input) {
		return match(input, PATTERN, new IStackElementFactory<More>() {

			@Override
			public More newInstance(String line, Map<String, String> attributes) {
				return new More(line, attributes);
			}
		});
	}

}
