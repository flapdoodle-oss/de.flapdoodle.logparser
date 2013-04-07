package de.flapdoodle.logparser.matcher;

import static de.flapdoodle.logparser.regex.Patterns.build;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static java.util.regex.Pattern.compile;

import java.util.logging.Level;
import java.util.regex.Pattern;

public class CustomPatterns {

//	private static final String SMALL_CAPS = "a-z";
//	private static final String ALL_CAPS = "\\w";
//	public static final Pattern Package = build("[",SMALL_CAPS,"]+");
//	public static final Pattern Packages = join(Package, join(build("(", "\\.", Package.pattern(), ")*")));

	public static final Pattern Method = compile("([a-zA-Z]+)");		
	public static final Pattern Classname = compile("([a-zA-Z]+)((\\.|\\$)[a-zA-Z]+)*");
	public static final Pattern Date = compile("([a-zA-Z]+) ([0-2]\\d|3[01]), (19|20|21)(\\d\\d) (1\\d|2\\d|\\d):([0-5][0-9]):([0-5][0-9]) (AM|PM)");
	public static final Pattern Levels = compile(allLevels());


	
	
	private CustomPatterns() {
		// no instance
	}
	
	static String allLevels() {
		StringBuilder sb=new StringBuilder();
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
