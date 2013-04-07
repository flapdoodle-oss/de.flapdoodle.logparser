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
package de.flapdoodle.logparser.regex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Patterns {
	static final Logger _logger=Logger.getLogger(Pattern.class.getName());
	
	
	static final IPatternNameSetExtractor PATTERN_NAME_SET_EXTRACTOR;
	static {
		IPatternNameSetExtractor instance = new ParsePatternForGroupNamesNameSetExtractor();
		try {
			instance = new ProtectedMethodCallSetExtractor();
		} catch (RuntimeException ex) {
			_logger.log(Level.WARNING,"choose pattern name set extractor",ex);
		}
		PATTERN_NAME_SET_EXTRACTOR=instance;
	}

	private Patterns() {
		// no instance
	}


	public static Set<String> names(Pattern pattern) {
		return PATTERN_NAME_SET_EXTRACTOR.names(pattern);
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

	@VisibleForTesting
	protected static interface IPatternNameSetExtractor {
		Set<String> names(Pattern pattern);
	}
	
	@VisibleForTesting
	protected static class ParsePatternForGroupNamesNameSetExtractor implements IPatternNameSetExtractor {

		final Pattern GROUP_NAMES_PATTERN = Pattern.compile("\\?\\<([a-zA-Z0-9]+)\\>");
		
		protected Set<String> namesFallback(Pattern pattern) {
			Set<String> ret=Sets.newHashSet();
			Matcher groupNamesMatcher = GROUP_NAMES_PATTERN.matcher(pattern.pattern());
			while (groupNamesMatcher.find()) {
				ret.add(groupNamesMatcher.group(1));
			}
			return ret;
		}
		
		@Override
		public Set<String> names(Pattern pattern) {
			return ImmutableSet.copyOf(namesFallback(pattern));
		}
		
	}
	
	@VisibleForTesting
	protected static class ProtectedMethodCallSetExtractor implements IPatternNameSetExtractor {
		
		final Method PATTERN_NAMED_GROUP_METHOD;
		{
			try {
				PATTERN_NAMED_GROUP_METHOD = Pattern.class.getDeclaredMethod("namedGroups");
				PATTERN_NAMED_GROUP_METHOD.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}
		
		private Map<String, Integer> callProtectedNamedGroupsMethod(Pattern pattern) {
			try {
				return (Map<String, Integer>) PATTERN_NAMED_GROUP_METHOD.invoke(pattern);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public Set<String> names(Pattern pattern) {
			return ImmutableSet.copyOf(callProtectedNamedGroupsMethod(pattern).keySet());
		}
	}
}
