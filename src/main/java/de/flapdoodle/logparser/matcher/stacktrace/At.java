package de.flapdoodle.logparser.matcher.stacktrace;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Classname;
import static de.flapdoodle.logparser.matcher.CustomPatterns.Method;
import static de.flapdoodle.logparser.matcher.CustomPatterns.Space;
import static de.flapdoodle.logparser.regex.Patterns.group;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.regex.Patterns;

public class At extends AbstractStackElement {

	private static final String LINE_NR = "line";
	private static final String FILE = "file";
	private static final String METHOD = "method";
	private static final String CLASSNAME = "class";
	
	private static final Pattern PATTERN = join(
			compile("^"),
			Space,
			compile("at"),
			Space,
			namedGroup(CLASSNAME, Classname),
			compile("\\."),
			namedGroup(METHOD, Method),
			compile("\\("),
			group(compile("Native Method"),compile("|"), namedGroup(FILE, "([a-zA-Z][a-zA-Z0-9]+)\\.java"), compile(":"),
					namedGroup(LINE_NR, "\\d+")), compile("\\)"));

	protected At(String line, Map<String, String> attributes) {
		super(line, attributes);
	}

	public String classname() {
		return attribute(CLASSNAME);
	}
	
	public String method() {
		return attribute(METHOD);
	}
	
	public String file() {
		return attribute(FILE);
	}
	
	public String lineNr() {
		return attribute(LINE_NR);
	}
	
	public static boolean find(CharSequence input) {
		return Patterns.find(PATTERN, input);
	}
	
	public static Optional<At> match(CharSequence input) {
		return match(input,PATTERN,new IStackElementFactory<At>() {
			@Override
			public At newInstance(String line, Map<String, String> attributes) {
				return new At(line,attributes);
			}
		});
	}


}
