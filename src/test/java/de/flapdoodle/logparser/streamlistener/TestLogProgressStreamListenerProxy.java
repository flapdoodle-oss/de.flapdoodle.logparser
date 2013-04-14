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

import static org.junit.Assert.*;

import java.lang.ref.Reference;

import org.junit.Test;

import de.flapdoodle.logparser.IStreamListener;

public class TestLogProgressStreamListenerProxy {

	@Test
	public void loopUntilOutput() {

		int loopsBeforeOutput = 10;

		CountCallsStreamListener<String> countingListener = new CountCallsStreamListener<>();

		LogProgressStreamListenerProxyTestInstance<String> streamListener = new LogProgressStreamListenerProxyTestInstance<>(
				countingListener, loopsBeforeOutput);

		for (int i = 0; i < 4 * loopsBeforeOutput; i++) {
			streamListener.entry("dummy");
		}

		assertEquals("proxy called", 40, countingListener.called());
		assertEquals("proxy called", 4, streamListener.showProgressCalled());
	}

	private final class LogProgressStreamListenerProxyTestInstance<T> extends LogProgressStreamListenerProxy<T> {

		int _showProgressCalled;

		private LogProgressStreamListenerProxyTestInstance(IStreamListener<T> proxiedStreamListener,
				int nrOfEntriesBeforeOutput) {
			super(proxiedStreamListener, nrOfEntriesBeforeOutput);
		}

		@Override
		protected void showProgress(long entries) {
			_showProgressCalled++;
			super.showProgress(entries);
		}

		public int showProgressCalled() {
			return _showProgressCalled;
		}
	}

	static class CountCallsStreamListener<T> implements IStreamListener<T> {

		private long _called;

		@Override
		public void entry(T value) {
			_called++;
		}

		public long called() {
			return _called;
		}
	}
}
