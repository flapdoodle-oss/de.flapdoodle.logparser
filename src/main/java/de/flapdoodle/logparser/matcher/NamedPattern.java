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
