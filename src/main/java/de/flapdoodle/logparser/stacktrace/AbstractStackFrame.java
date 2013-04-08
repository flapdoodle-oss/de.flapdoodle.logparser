package de.flapdoodle.logparser.stacktrace;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;


public abstract class AbstractStackFrame {

	protected final ExceptionAndMessage _exceptionAndMessage;
	protected final ImmutableList<At> _stack;
	protected final CauseBy _cause;

	protected AbstractStackFrame(ExceptionAndMessage exceptionAndMessage, List<At> stack, CauseBy cause) {
		_exceptionAndMessage = exceptionAndMessage;
		_cause = cause;
		_stack = ImmutableList.copyOf(stack);
	}

	public ExceptionAndMessage exception() {
		return _exceptionAndMessage;
	}

	public Optional<CauseBy> cause() {
		return Optional.fromNullable(_cause);
	}
	
	public AbstractStackFrame rootCause() {
		if (_cause!=null) {
			return _cause.rootCause();
		}
		return this;
	}
	
	public Optional<At> firstAt() {
		return !_stack.isEmpty() ? Optional.of(_stack.get(0)) : Optional.<At>absent();
	}


	public ImmutableList<At> stack() {
		return _stack;
	}

}
