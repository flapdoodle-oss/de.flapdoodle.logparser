package de.flapdoodle.logparser;

import java.io.IOException;
import java.util.List;

public class StreamMatch implements IMatch<StreamEntry> {

	private final String _message;

	public StreamMatch(String message) {
		_message = message;
	}

	@Override
	public StreamEntry process(List<String> lines) throws IOException, StreamProcessException {
		return new StreamEntry(_message,lines);
	}
	
}