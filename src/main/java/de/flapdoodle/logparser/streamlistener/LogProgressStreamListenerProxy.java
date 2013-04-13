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

	private final IStreamListener<T> _proxiedStreamListener;
	private final int _nrOfEntriesBeforeOutput;

	long _processedEntries=0;
	long _nextOutput;
	
	public LogProgressStreamListenerProxy(IStreamListener<T> proxiedStreamListener,int nrOfEntriesBeforeOutput) {
		_proxiedStreamListener = proxiedStreamListener;
		_nrOfEntriesBeforeOutput = nrOfEntriesBeforeOutput;
		setNextMark();
	}

	@Override
	public void entry(T value) {
		_proxiedStreamListener.entry(value);
		
		_processedEntries++;
		
		if (markReached()) {
			showProgress(_processedEntries);
			setNextMark();
		}
	}

	private boolean markReached() {
		return _nextOutput<=_processedEntries;
	}

	private void setNextMark() {
		_nextOutput=_processedEntries+_nrOfEntriesBeforeOutput;
	}
	
	protected void showProgress(long entries) {
		System.out.print("processed "+entries+"\r");
	}

}
