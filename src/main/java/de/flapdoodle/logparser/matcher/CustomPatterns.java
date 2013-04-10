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
package de.flapdoodle.logparser.matcher;

import java.util.logging.Level;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class CustomPatterns {

	//	private static final String SMALL_CAPS = "a-z";
	//	private static final String ALL_CAPS = "\\w";
	//	public static final Pattern Package = build("[",SMALL_CAPS,"]+");
	//	public static final Pattern Packages = join(Package, join(build("(", "\\.", Package.pattern(), ")*")));

	public static final Pattern Space = compile("\\s+");

	public static final Pattern Method = compile("([a-zA-Z][a-zA-Z0-9\\$]*)");
	public static final Pattern Classname = compile("([a-zA-Z]+)((\\.|\\$)[a-zA-Z\\$][a-zA-Z\\$0-9]*)*");

	public static final Pattern Date = compile("([a-zA-Z]+) ([0-2]\\d|3[01]), (19|20|21)(\\d\\d) (1\\d|2\\d|\\d):([0-5][0-9]):([0-5][0-9]) (AM|PM)");
	public static final Pattern Levels = compile(allLevels());

	private CustomPatterns() {
		// no instance
	}

	static String allLevels() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		appendLevel(sb, Level.CONFIG);
		sb.append("|");
		appendLevel(sb, Level.FINE);
		sb.append("|");
		appendLevel(sb, Level.FINER);
		sb.append("|");
		appendLevel(sb, Level.FINEST);
		sb.append("|");
		appendLevel(sb, Level.INFO);
		sb.append("|");
		appendLevel(sb, Level.SEVERE);
		sb.append("|");
		appendLevel(sb, Level.WARNING);
		sb.append(")");
		return sb.toString();
	}

	static void appendLevel(StringBuilder sb, Level level) {
		sb.append(level.getName());
	}

}
