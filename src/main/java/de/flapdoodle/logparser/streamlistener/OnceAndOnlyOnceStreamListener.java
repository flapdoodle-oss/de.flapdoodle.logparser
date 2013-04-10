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
package de.flapdoodle.logparser.streamlistener;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IStreamListener;

public class OnceAndOnlyOnceStreamListener<T> implements IStreamListener<T> {

	boolean firstCall = true;
	private T _value;

	@Override
	public void entry(T value) {
		if (!firstCall)
			throw new IllegalArgumentException("called more than once: [" + _value + "]");

		firstCall = false;
		_value = value;
	}

	public Optional<T> value() {
		return Optional.fromNullable(_value);
	}

}
