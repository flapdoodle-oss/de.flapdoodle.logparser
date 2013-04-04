package de.flapdoodle.logparser.stacktrace;

public class ExceptionAndMessage {

	private final String _exception;
	private final String _message;

	public ExceptionAndMessage(String exception, String message) {
		_exception = exception;
		_message = message;
	}
}