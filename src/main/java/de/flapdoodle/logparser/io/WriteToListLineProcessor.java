package de.flapdoodle.logparser.io;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.ILineProcessor;


public class WriteToListLineProcessor implements ILineProcessor {

	List<String> _lines=Lists.newArrayList();
	
	@Override
	public void processLine(String line) {
		_lines.add(line);
	}
	
	public ImmutableList<String> lines() {
		return ImmutableList.copyOf(_lines);
	}

}
