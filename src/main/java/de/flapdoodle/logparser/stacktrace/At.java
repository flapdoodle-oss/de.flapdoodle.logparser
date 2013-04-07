/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * with contributions from
 * 	${lic.developers}
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	
	@Override
	public String toString() {
		return "\tat "+_classname+"."+_method+"("+(((_file!=null) && (_line!=null)) ? ""+_file+":"+_line : "???")+")"; 
	}
}