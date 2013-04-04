package de.flapdoodle.logparser.stacktrace;

import java.util.List;

public class Cause {

	private final Cause _cause;
	private final List<At> _stack;
	private final ExceptionAndMessage _exceptionAndMessage;
	private final More _more;

	public Cause(ExceptionAndMessage exceptionAndMessage, List<At> stack, Cause cause, More more) {
		_exceptionAndMessage = exceptionAndMessage;
		_cause = cause;
		_stack = stack;
		_more = more;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Caused by: ");
		sb.append(_exceptionAndMessage);
		sb.append("\n");
		for (At at : _stack) {
			sb.append(at);
			sb.append("\n");
		}
		if (_more != null) {
			sb.append(_more);
			sb.append("\n");
		}
		if (_cause != null) {
			sb.append(_cause);
			sb.append("\n");
		}
		return sb.toString();
	}
}
