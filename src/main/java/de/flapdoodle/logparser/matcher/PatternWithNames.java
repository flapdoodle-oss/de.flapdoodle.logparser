package de.flapdoodle.logparser.matcher;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class PatternWithNames implements IPatternWithNames {

	private final Pattern _pattern;
	private final Set<String> _names;

	public PatternWithNames(String regex) {
		this(regex, Collections.<String> emptySet());
	}

	public PatternWithNames(String regex, Set<String> names) {
		_pattern = Pattern.compile(regex);
		_names = ImmutableSet.copyOf(names);
	}

	public PatternWithNames(String regex, String... names) {
		this(regex, Sets.newHashSet(names));
	}

	@Override
	public String patternAsString() {
		return _pattern.pattern();
	}

	@Override
	public Set<String> names() {
		return _names;
	}

	@Override
	public boolean find(CharSequence input) {
		return _pattern.matcher(input).find();
	}

	@Override
	public Optional<Map<String, String>> parse(CharSequence input) {
		Matcher matcher = _pattern.matcher(input);
		if (matcher.find()) {
			Map<String, String> map = Maps.newHashMap();
			for (String name : _names) {
				map.put(name, matcher.group(name));
			}
			return Optional.of(map);
		}
		return Optional.absent();
	}

	public static IPatternWithNames join(IPatternWithNames... patterns) {
		StringBuilder sb = new StringBuilder();
		Set<String> names = Collections.emptySet();

		for (IPatternWithNames p : patterns) {
			SetView<String> union = Sets.union(names, p.names());
			if (union.size() != names.size() + p.names().size()) {
				throw new IllegalArgumentException("name collision " + Sets.intersection(names, p.names()));
			}
			sb.append(p.patternAsString());
			names = union;
		}

		return new PatternWithNames(sb.toString(), names);
	}
	
	public static IPatternWithNames namedGroup(String name,IPatternWithNames... patterns) {
		return join(temp("(?<"+name+">",name),join(patterns),temp(")"));
	}
	
	static IPatternWithNames temp(String regex,String...names) {
		return new NonValidatedTempPatternWithNames(regex, names);
	}
	
	static class NonValidatedTempPatternWithNames implements IPatternWithNames {

		private final String _regex;
		private final Set<String> _names;

		protected NonValidatedTempPatternWithNames(String regex,String... names) {
			_regex = regex;
			_names=ImmutableSet.copyOf(Sets.newHashSet(names));
		}
		
		@Override
		public String patternAsString() {
			return _regex;
		}

		@Override
		public Set<String> names() {
			return _names;
		}

		@Override
		public boolean find(CharSequence input) {
			throw new IllegalArgumentException("not implemented");
		}

		@Override
		public Optional<Map<String, String>> parse(CharSequence input) {
			throw new IllegalArgumentException("not implemented");
		}
		
	}
}
