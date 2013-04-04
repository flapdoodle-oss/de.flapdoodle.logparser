package de.flapdoodle.logparser.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;


public final class Streams {

	private Streams() {
		// no instance
	}
	
	public static InputStream compressed(InputStream source) throws IOException {
		return new GZIPInputStream(source);
	}
}
