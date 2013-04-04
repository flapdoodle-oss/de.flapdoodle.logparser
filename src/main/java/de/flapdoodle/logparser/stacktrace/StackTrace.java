package de.flapdoodle.logparser.stacktrace;

import java.util.List;

public class StackTrace {

	private final List<String> _source;
	private final ExceptionAndMessage _exceptionAndMessage;
	private final List<At> _stack;
	private final Cause _cause;

	public StackTrace(List<String> source, ExceptionAndMessage exceptionAndMessage, List<At> stack, Cause cause) {
		_source = source;
		_exceptionAndMessage = exceptionAndMessage;
		_stack = stack;
		_cause = cause;
	}
}
