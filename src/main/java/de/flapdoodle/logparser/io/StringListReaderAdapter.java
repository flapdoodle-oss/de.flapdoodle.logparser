package de.flapdoodle.logparser.io;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.logparser.IRewindableReader;

public class StringListReaderAdapter implements IRewindableReader {

	private final List<String> _lines;
	int _idx = 0;
	int _marker=-1;

	public StringListReaderAdapter(List<String> lines) {
		_lines = ImmutableList.copyOf(lines);
	}

	@Override
	public synchronized Optional<String> nextLine() throws IOException {
		if (_idx < _lines.size()) {
			String line = _lines.get(_idx);
			_idx++;
			return Optional.of(line);
		}
		return Optional.absent();
	}

	@Override
	public synchronized void setMarker() throws IOException {
		_marker=_idx;
	}

	@Override
	public synchronized void jumpToMarker() throws IOException {
		if (_marker==-1) throw new IOException("marker not set");
		_idx=_marker;
	}

}
