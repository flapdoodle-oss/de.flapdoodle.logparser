/**
 * Copyright (C) 2013
 * Michael Mosmann <michael@mosmann.de>
 * 
 * with contributions from
 * ${lic.developers}
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.stacktrace;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.util.List;

public abstract class AbstractStackFrame {

	protected final ExceptionAndMessage _exceptionAndMessage;
	protected final ImmutableList<StackLines> _stackLines;
	protected final CauseBy _cause;

	protected AbstractStackFrame(ExceptionAndMessage exceptionAndMessage, List<StackLines> stackLines, CauseBy cause) {
		if (stackLines.isEmpty()) {
			throw new RuntimeException("stacklines emtpy");
		}
		_exceptionAndMessage = exceptionAndMessage;
		_cause = cause;
		_stackLines = ImmutableList.copyOf(stackLines);
	}

	public ExceptionAndMessage exception() {
		return _exceptionAndMessage;
	}

	public Optional<CauseBy> cause() {
		return Optional.fromNullable(_cause);
	}

	public AbstractStackFrame rootCause() {
		if (_cause != null) {
			return _cause.rootCause();
		}
		return this;
	}

	public ImmutableList<StackLines> stackLines() {
		return _stackLines;
	}

	public Optional<At> firstAt() {
		return firstStackLines().firstAt();
	}

	public StackLines firstStackLines() {
		return _stackLines.get(0);
	}
}
