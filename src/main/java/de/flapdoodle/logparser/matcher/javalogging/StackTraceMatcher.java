package de.flapdoodle.logparser.matcher.javalogging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.regex.Patterns;

public class StackTraceMatcher implements IMatcher<String> {

	private static final String COUNT_GROUP_ID = "COUNT";
	private static final String METHOD_GROUP_ID = "METHOD";
	private static final String FILE_GROUP_ID = "FILE";
	private static final String MESSAGE_GROUP_ID = "MESSAGE";
	private static final String LINE_GROUP_ID = "LINE";
	private static final String CLASSNAME_GROUP_ID = "CLASSNAME";

	private static final String CLASSNAME_PATTERN = "(?<" + CLASSNAME_GROUP_ID + ">([a-zA-Z]+)((\\.|\\$)[a-zA-Z][a-zA-Z\\$0-9]*)*)";

	static final Pattern AT_LINE_PATTERN = Pattern.compile("^(\\s+)at(\\s+)" + CLASSNAME_PATTERN + "\\.(?<"
			+ METHOD_GROUP_ID + ">([a-zA-Z][a-zA-Z0-9\\$]*))" + "\\(((Native Method)|((?<" + FILE_GROUP_ID
			+ ">([a-zA-Z][a-zA-Z0-9]+)\\.java):(?<" + LINE_GROUP_ID + ">\\d+)))\\)$");

	static final Pattern EXCEPTION_PATTERN = Pattern.compile(CLASSNAME_PATTERN + ":(?<" + MESSAGE_GROUP_ID + ">.*)$");

	static final Pattern CAUSE_BY_PATTERN = Pattern.compile("Caused(\\s+)by:(\\s+)" + CLASSNAME_PATTERN + ":(?<"
			+ MESSAGE_GROUP_ID + ">.*)$");

	static final Pattern MORE_PATTERN = Pattern.compile("(\\s+)\\.\\.\\.(\\s+)(?<" + COUNT_GROUP_ID + ">\\d+) more");

	@Override
	public Optional<IMatch<String>> match(IReader reader) throws IOException {
		Optional<String> possibleFirstLine = reader.nextLine();
		if (possibleFirstLine.isPresent()) {
			String firstLine = possibleFirstLine.get();
			if (Patterns.find(CAUSE_BY_PATTERN, firstLine)) {
				return Optional.absent();
			}
			if (Patterns.find(AT_LINE_PATTERN, firstLine)) {
				return Optional.absent();
			}
			if (Patterns.find(MORE_PATTERN, firstLine)) {
				return Optional.absent();
			}

			Optional<String> secondLine = reader.nextLine();
			if (secondLine.isPresent()) {
				Optional<Map<String, String>> secondMatch = Patterns.match(AT_LINE_PATTERN.matcher(secondLine.get()));
				if (secondMatch.isPresent()) {
					Optional<Map<String, String>> firstMatch = Patterns.match(EXCEPTION_PATTERN.matcher(firstLine));
					if (firstMatch.isPresent()) {
						return Optional.<IMatch<String>> of(new StackTraceMatch(firstLine, secondLine.get()));
					}
				}
			}
		}
		return Optional.absent();
	}

	static class StackTraceMatch implements IMatch<String> {

		private final String _firstLine;
		private final String _atLine;

		public StackTraceMatch(String firstLine, String atLine) {
			_firstLine = firstLine;
			_atLine = atLine;
		}

		@Override
		public String process(List<String> lines) throws IOException {
			List<String> all = Lists.newArrayList();
			all.add(_firstLine);
			all.add(_atLine);
			all.addAll(lines);
			return toString(all);
		}

		private String toString(List<String> all) {
			StringBuilder sb=new StringBuilder();
			for (String line : all) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		}

	}
}
