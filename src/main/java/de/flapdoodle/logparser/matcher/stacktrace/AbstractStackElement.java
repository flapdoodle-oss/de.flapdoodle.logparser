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
package de.flapdoodle.logparser.matcher.stacktrace;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import de.flapdoodle.logparser.regex.Patterns;

public abstract class AbstractStackElement {

	private final String _line;
	private final ImmutableMap<String, String> _attributes;

	protected AbstractStackElement(String line, Map<String, String> attributes) {
		_line = line;
		_attributes = ImmutableMap.copyOf(attributes);
	}

	protected String attribute(String key) {
		return _attributes.get(key);
	}
	
	public ImmutableMap<String, String> attributes() {
		return _attributes;
	}

	public String line() {
		return _line;
	}
	
	static interface IStackElementFactory<T> {
		T newInstance(String line,Map<String,String> attributes);
	}
	
	protected static <T> Optional<T> match(CharSequence input,Pattern pattern, IStackElementFactory<T> factory) {
		Optional<Map<String, String>> m = Patterns.match(pattern.matcher(input));
		if (m.isPresent()) {
			return Optional.of(factory.newInstance(input.toString(),m.get()));
		}
		return Optional.absent();
	}

}
