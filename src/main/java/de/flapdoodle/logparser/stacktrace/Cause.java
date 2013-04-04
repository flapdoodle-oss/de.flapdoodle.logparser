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
}