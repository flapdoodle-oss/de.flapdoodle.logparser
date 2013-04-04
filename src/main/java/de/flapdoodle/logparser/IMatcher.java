package de.flapdoodle.logparser;

import java.io.IOException;
import java.util.List;

public interface IMatcher {

	boolean match(String firstLine);

	void process(List<String> lines) throws IOException;
}
