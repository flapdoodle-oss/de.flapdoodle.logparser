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

import de.flapdoodle.logparser.IStreamListener;


public class LogProgressStreamListenerProxy<T> implements IStreamListener<T> {

	static final int MIN_NUMBER_OF_ENTRIES_PROCESSED_BEFORE_OUTPUT=1000;
	
	long _processedEntries=0;
	long _nextOutput=_processedEntries+MIN_NUMBER_OF_ENTRIES_PROCESSED_BEFORE_OUTPUT;
	
	
	private final IStreamListener<T> _proxiedStreamListener;

	public LogProgressStreamListenerProxy(IStreamListener<T> proxiedStreamListener) {
		_proxiedStreamListener = proxiedStreamListener;
	}
	
	@Override
	public void entry(T value) {
		_proxiedStreamListener.entry(value);
		_processedEntries++;
		if (_nextOutput<=_processedEntries) {
			System.out.print("processed "+_processedEntries+"\r");
			_nextOutput=_processedEntries+MIN_NUMBER_OF_ENTRIES_PROCESSED_BEFORE_OUTPUT;
		}
	}

}
