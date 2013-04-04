package de.flapdoodle.logparser.matcher.javalogging;

import de.flapdoodle.logparser.matcher.NamedPattern;


public class ClassPattern extends NamedPattern {
	public ClassPattern() {
		super("class","(?<class>([a-zA-Z]+)((\\.|\\$)[a-zA-Z]+)*)");
	}
}
