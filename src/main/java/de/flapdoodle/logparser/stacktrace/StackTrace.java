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

	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(_exceptionAndMessage);
		sb.append("\n");
		for (At at: _stack) {
			sb.append(at);
			sb.append("\n");
		}
		if (_cause!=null) {
			sb.append(_cause);
			sb.append("\n");
		}
		return sb.toString();
	}
}
