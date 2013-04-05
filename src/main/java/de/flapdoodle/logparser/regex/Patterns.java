package de.flapdoodle.logparser.regex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Patterns {

	static final Method PATTERN_NAMED_GROUP_METHOD;
	static {
		try {
			PATTERN_NAMED_GROUP_METHOD = Pattern.class.getDeclaredMethod("namedGroups");
			PATTERN_NAMED_GROUP_METHOD.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private Patterns() {
		// no instance
	}

	private static Map<String, Integer> callProtectedNamedGroupsMethod(Pattern pattern) {
		try {
			return (Map<String, Integer>) PATTERN_NAMED_GROUP_METHOD.invoke(pattern);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Set<String> names(Pattern pattern) {
		return ImmutableSet.copyOf(callProtectedNamedGroupsMethod(pattern).keySet());
	}

	public static boolean find(Pattern pattern, String input) {
		return pattern.matcher(input).find();
	}
	
	public static Optional<Map<String, String>> match(Matcher matcher) {
		if (matcher.find()) {
			Map<String, String> map = Maps.newHashMap();
			for (String name : names(matcher.pattern())) {
				map.put(name, matcher.group(name));
			}
			return Optional.of(map);
		}
		return Optional.absent();
	}

	public static Pattern build(Collection<String> parts) {
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			sb.append(part);
		}
		return Pattern.compile(sb.toString());
	}

	public static Pattern build(String... parts) {
		return build(asCollection(parts));
	}

	public static Pattern join(Pattern... patterns) {
		return join(asCollection(patterns));
	}

	public static Pattern join(Collection<Pattern> patterns) {
		return build(asStrings(patterns));
	}

	public static Pattern namedGroup(String name, Pattern... patterns) {
		return build(join(asCollection("(?<", name, ">"), asStrings(asCollection(patterns)), asCollection(")")));
	}
	
	private static Collection<String> asStrings(Collection<Pattern> patterns) {
		return Collections2.transform(patterns, new Function<Pattern, String>() {

			@Override
			public String apply(Pattern input) {
				return input.pattern();
			}
		});
	}

	private static <T> Collection<T> join(T before, Collection<T> collections, T... after) {
		ArrayList<T> ret = Lists.newArrayList();
		ret.add(before);
		ret.addAll(collections);
		for (T a : after) {
			ret.add(a);
		}
		return ret;
	}

	private static <T> Collection<T> join(Collection<T>... collections) {
		ArrayList<T> ret = Lists.newArrayList();
		for (Collection<T> collection : collections) {
			ret.addAll(collection);
		}
		return ret;
	}

	private static <T> Collection<T> asCollection(T... values) {
		return Lists.newArrayList(values);
	}

}
