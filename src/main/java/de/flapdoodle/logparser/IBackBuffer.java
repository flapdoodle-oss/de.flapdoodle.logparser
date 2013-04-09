package de.flapdoodle.logparser;

import com.google.common.collect.ImmutableList;

public interface IBackBuffer {

	/**
	 * return last line first, second last line second, ..
	 * 
	 * @return list of lines in reverse order
	 */
	ImmutableList<String> lastLines();
}
