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
