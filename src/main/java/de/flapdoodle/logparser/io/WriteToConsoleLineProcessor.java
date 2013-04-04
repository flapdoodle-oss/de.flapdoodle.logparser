package de.flapdoodle.logparser.io;

import de.flapdoodle.logparser.ILineProcessor;

public class WriteToConsoleLineProcessor implements ILineProcessor {

	@Override
	public void processLine(String line) {
		System.out.println(line);
	}

}
