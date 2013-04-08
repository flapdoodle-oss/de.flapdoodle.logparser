package de.flapdoodle.logparser.matcher.stacktrace;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Classname;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.matcher.stacktrace.AbstractStackElement.IStackElementFactory;
import de.flapdoodle.logparser.regex.Patterns;

public class FirstLine extends AbstractStackElement {

	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	
	static final Pattern PATTERN = join(namedGroup(EXCEPTION, Classname), compile(": "),
			namedGroup(MESSAGE, compile(".*$")));

	protected FirstLine(String line, Map<String, String> map) {
		super(line,map);
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
		return match(input,PATTERN,new IStackElementFactory<FirstLine>() {
			@Override
			public FirstLine newInstance(String line, Map<String, String> attributes) {
				return new FirstLine(line,attributes);
			}
		});
	}
	
}
