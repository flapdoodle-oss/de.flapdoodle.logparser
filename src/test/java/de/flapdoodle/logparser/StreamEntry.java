package de.flapdoodle.logparser;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class StreamEntry {

	private final String _message;
	private final ImmutableList<String> _comments;

	public StreamEntry(String message, List<String> comments) {
		_message = message;
		_comments = ImmutableList.copyOf(comments);
	}
	
	public String message() {
		return _message;
	}
	
	public ImmutableList<String> comments() {
		return _comments;
	}
	
}