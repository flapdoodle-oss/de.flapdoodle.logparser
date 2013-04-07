/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *   ${lic.username2} <${lic.email2}>
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
package de.flapdoodle.logparser.matcher;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class NamedPatterns {

	private final NamedPattern[] _patterns;
	private Pattern _pattern;

	public NamedPatterns(NamedPattern... patterns) {
		_patterns = patterns;
		StringBuilder sb=new StringBuilder();
		for (NamedPattern p : patterns) {
			sb.append(p.patternAsString());
		}
		_pattern = Pattern.compile(sb.toString());
	}
	
	public boolean find(CharSequence input) {
		return _pattern.matcher(input).find();
	}
	
	public Optional<Map<String,String>> parse(CharSequence input) {
		Matcher matcher = _pattern.matcher(input);
		if (matcher.find()) {
			Map<String, String> map=Maps.newHashMap();
			for (NamedPattern p : _patterns) {
				String name = p.name();
				if (name!=null) {
					map.put(name, matcher.group(name));
				}
			}
			return Optional.of(map);
		}
		return Optional.absent();
	}
}
