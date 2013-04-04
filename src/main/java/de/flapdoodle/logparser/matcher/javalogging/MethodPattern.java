package de.flapdoodle.logparser.matcher.javalogging;

import de.flapdoodle.logparser.matcher.NamedPattern;


public class MethodPattern extends NamedPattern {
	public MethodPattern() {
		super("method","([a-zA-Z]+)");
	}
}
