package de.flapdoodle.logparser.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IReader;

public class BufferedReaderAdapter implements IReader {

	private final BufferedReader _reader;
	private final int _readAheadLimit;

	public BufferedReaderAdapter(InputStream stream, Charset charset,int readAheadLimit) {
		_readAheadLimit = readAheadLimit;
		_reader = new BufferedReader(new InputStreamReader(stream,charset));
	}

	@Override
	public Optional<String> nextLine() throws IOException {
		String line=_reader.readLine();
		return line!=null ? Optional.of(line) : Optional.<String>absent();
	}

	@Override
	public void setMarker() throws IOException {
		_reader.mark(_readAheadLimit);
	}
	
	@Override
	public void jumpToMarker() throws IOException {
		_reader.reset();
	}
	
	

	
}