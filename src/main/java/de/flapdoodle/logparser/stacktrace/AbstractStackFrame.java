/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
