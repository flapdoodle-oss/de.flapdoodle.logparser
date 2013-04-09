package de.flapdoodle.logparser;

import java.io.IOException;

public interface IStreamProcessor<T> {

	void process(IRewindableReader reader) throws IOException;

}
