package de.flapdoodle.logparser.stacktrace;

public class At {

	private final String _classname;
	private final String _method;
	private final String _file;
	private final Integer _line;

	public At(String classname, String method, String file, String line) {
		_classname = classname;
		_method = method;
		_file = file;
		_line = line != null
				? Integer.valueOf(line)
				: null;
	}
}