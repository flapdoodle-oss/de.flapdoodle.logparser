package de.flapdoodle.logparser.matcher.javalogging;

import java.util.logging.Level;

import de.flapdoodle.logparser.matcher.NamedPattern;


public class LevelPattern extends NamedPattern {

	public LevelPattern() {
		super("level", allLevels());
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
