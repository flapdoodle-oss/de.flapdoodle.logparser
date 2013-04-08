package de.flapdoodle.logparser.matcher.stacktrace;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Classname;
import static de.flapdoodle.logparser.matcher.CustomPatterns.Space;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.regex.Patterns;

public class CauseBy extends AbstractStackElement {

	private static final String EXCEPTION = "exception";
	private static final String MESSAGE = "message";
	
	private static final Pattern PATTERN = join(compile("Caused"), Space, compile("by:"), Space,
			namedGroup(EXCEPTION, Classname), compile(": "), namedGroup(MESSAGE, compile(".*$")));

	protected CauseBy(String line, Map<String, String> attributes) {
		super(line, attributes);
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

	public static Optional<CauseBy> match(CharSequence input) {
		return match(input,PATTERN,new IStackElementFactory<CauseBy>() {
			@Override
			public CauseBy newInstance(String line, Map<String, String> attributes) {
				return new CauseBy(line,attributes);
			}
		});
	}


}
