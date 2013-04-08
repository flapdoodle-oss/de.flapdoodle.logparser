package de.flapdoodle.logparser.streamlistener;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IStreamListener;

public class OnceAndOnlyOnceStreamListener<T> implements IStreamListener<T> {

	boolean firstCall=true;
	private T _value;

	@Override
	public void entry(T value) {
		if (!firstCall) throw new IllegalArgumentException("called more than once");
		
		firstCall=false;
		_value=value;
	}

	public Optional<T> value() {
		return Optional.fromNullable(_value);
	}
	
}