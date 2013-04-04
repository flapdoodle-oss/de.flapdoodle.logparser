package de.flapdoodle.logparser.matcher.javalogging;

import de.flapdoodle.logparser.matcher.NamedPattern;

public class DatePattern extends NamedPattern {

	// Apr 04, 2013 8:15:44 PM
	public DatePattern() {
		super("date", "([a-zA-Z]+) ([0-2]\\d|3[01]), (19|20|21)(\\d\\d) (1\\d|2\\d|\\d):([0-5][0-9]):([0-5][0-9]) (AM|PM)");
	}
	
}