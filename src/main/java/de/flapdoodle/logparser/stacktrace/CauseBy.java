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

public class CauseBy extends AbstractStackFrame {

	private final More _more;

	public CauseBy(ExceptionAndMessage exceptionAndMessage, List<At> stack, CauseBy cause, More more) {
		super(exceptionAndMessage,stack,cause);
		_more = more;
	}

	public Optional<More> more() {
		return Optional.fromNullable(_more);
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