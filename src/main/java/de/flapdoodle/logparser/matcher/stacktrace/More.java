package de.flapdoodle.logparser.matcher.stacktrace;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Space;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static de.flapdoodle.logparser.regex.Patterns.namedGroup;
import static java.util.regex.Pattern.compile;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.regex.Patterns;

public class More extends AbstractStackElement {

	private static final String COUNT = "count";
	
	private static final Pattern PATTERN = join(Space, compile("\\.\\.\\."), Space, namedGroup(COUNT,compile("\\d+")), compile(" more"));

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
		return match(input,PATTERN,new IStackElementFactory<More>() {
			@Override
			public More newInstance(String line, Map<String, String> attributes) {
				return new More(line,attributes);
			}
		});
	}

}
