package de.flapdoodle.logparser.streamlistener;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.IStreamListener;

public class CollectingStreamListener<T> implements IStreamListener<T> {

	List<T> _entries = Lists.newArrayList();

	@Override
	public void entry(T value) {
		_entries.add(value);
	}

	public ImmutableList<T> entries() {
		return ImmutableList.copyOf(_entries);
	}

}
