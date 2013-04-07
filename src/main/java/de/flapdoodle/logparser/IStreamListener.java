package de.flapdoodle.logparser;


public interface IStreamListener<T> {
	void entry(T value);
}
