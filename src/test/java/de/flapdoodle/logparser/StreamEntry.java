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
package de.flapdoodle.logparser;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class StreamEntry {

	private final String _message;
	private final ImmutableList<String> _comments;

	public StreamEntry(String message, List<String> comments) {
		_message = message;
		_comments = ImmutableList.copyOf(comments);
	}
	
	public String message() {
		return _message;
	}
	
	public ImmutableList<String> comments() {
		return _comments;
	}
	
}