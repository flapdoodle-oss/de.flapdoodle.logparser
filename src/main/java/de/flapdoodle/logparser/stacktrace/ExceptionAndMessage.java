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

import com.google.common.collect.ImmutableList;

public class ExceptionAndMessage {

	private final String _exception;
	private final ImmutableList<String> _messages;

	public ExceptionAndMessage(String exception, List<String> messages) {
		_exception = exception;
		_messages = ImmutableList.copyOf(messages);
	}

	public String exceptionClass() {
		return _exception;
	}

	public ImmutableList<String> messages() {
		return _messages;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_exception);
		sb.append(":");
		for (String message : _messages) {
			sb.append(message);
			sb.append("\n");
		}
		return sb.toString();
	}
}
