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
package de.flapdoodle.logparser.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.IRewindableReader;

public class BufferedReaderAdapter implements IRewindableReader {

	private final BufferedReader _reader;
	private final int _readAheadLimit;
	private String _lastLine;
	private String _lastLineOnMarker;

	public BufferedReaderAdapter(InputStream stream, Charset charset, int readAheadLimit) {
		_readAheadLimit = readAheadLimit;
		_reader = new BufferedReader(new InputStreamReader(stream, charset));
	}

	@Override
	public Optional<String> nextLine() throws IOException {
		String line = _reader.readLine();
		_lastLine = line;
		return line != null
				? Optional.of(line)
				: Optional.<String> absent();
	}

	@Override
	public Optional<String> lastLine() {
		return Optional.fromNullable(_lastLine);
	}

	@Override
	public void setMarker() throws IOException {
		_reader.mark(_readAheadLimit);
		_lastLineOnMarker = _lastLine;
	}

	@Override
	public void jumpToMarker() throws IOException {
		_reader.reset();
		_lastLine = _lastLineOnMarker;
	}

}
