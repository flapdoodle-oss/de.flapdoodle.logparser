package de.flapdoodle.logparser;

import java.io.IOException;
import java.util.List;


public interface IMatch {
	void process(List<String> lines) throws IOException;
}
