package de.flapdoodle.logparser.matcher;

import java.util.regex.Pattern;
import static java.util.regex.Pattern.compile;

import de.flapdoodle.logparser.regex.Patterns;
import static de.flapdoodle.logparser.regex.Patterns.*;

public class CustomPatterns {

	private static final String SMALL_CAPS = "a-z";
	private static final String ALL_CAPS = "\\w";
	public static final Pattern Package = build("[",SMALL_CAPS,"]+");
	public static final Pattern Packages = join(Package, join(build("(", "\\.", Package.pattern(), ")*")));
	public static final Pattern Classname = join(Packages,build("\\.[A-Z]","[",ALL_CAPS,"]*"));
			
	private CustomPatterns() {
		// no instance
	}
}
