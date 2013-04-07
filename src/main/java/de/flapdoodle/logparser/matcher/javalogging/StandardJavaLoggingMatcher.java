package de.flapdoodle.logparser.matcher.javalogging;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.matcher.CustomPatterns;
import de.flapdoodle.logparser.regex.Patterns;
import de.flapdoodle.logparser.stacktrace.StackTrace;

public class StandardJavaLoggingMatcher implements IMatcher {

	//	NamedPatterns _firstLine = new NamedPatterns(new NamedPattern("^"), new DatePattern(), new NamedPattern("(\\s+)"),
	//			new ClassPattern(), new NamedPattern("(\\s+)"), new MethodPattern());

	Pattern _firstLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup("date", CustomPatterns.Date),
			Patterns.build("(\\s+)"), Patterns.namedGroup("class", CustomPatterns.Classname), Patterns.build("(\\s+)"),
			Patterns.namedGroup("method", CustomPatterns.Method));

	//	NamedPattern _levelPattern = new LevelPattern();
	//	NamedPatterns _secondLine = new NamedPatterns(new NamedPattern("^"), new LevelPattern(), new NamedPattern("message",
	//			"(?<message>.*)$"));

	Pattern _secondLine = Patterns.join(Patterns.build("^"), Patterns.namedGroup("level", CustomPatterns.Levels),
			Patterns.namedGroup("message", Patterns.build(".*")),Patterns.build("$"));

	@Override
	public boolean match(String firstLine) {
		//		int currentPosition = 0;

		return Patterns.find(_firstLine,firstLine);
		//		for (NamedPattern p : _patterns) {
		//			Matcher matcher = p.matcher(firstLine);
		//			if (matcher.find(currentPosition)) {
		////				System.out.println(p.name() + "=" + matcher.group());
		//				currentPosition = matcher.end();
		//			} else {
		//				return false;
		//			}
		//		}
		//		if (firstLine.length()>currentPosition) {
		//			System.out.println("left=" + firstLine.substring(currentPosition));
		//		}
	}

	@Override
	public void process(List<String> lines) throws IOException {
		String firstLine = lines.get(0);
		Optional<Map<String, String>> match = Patterns.match(_firstLine.matcher(firstLine));
		if (!match.isPresent())
			throw new IllegalArgumentException("Could not parse " + firstLine);

		if (lines.size() > 1) {

			String secondLine = lines.get(1);
			Optional<Map<String, String>> secondMatch = Patterns.match(_secondLine.matcher(secondLine));

			System.out.println(firstLine);
			System.out.println("--" + match.get());

			System.out.println(secondLine);
			System.out.println("--" + secondMatch.get());

			List<String> content = lines.subList(2, lines.size());

			Optional<StackTrace> stacktrace = StackTraceParser.parse(content);
			if (stacktrace.isPresent()) {
				System.out.println(">> stacktrace found: " + stacktrace.get());
			} else {

				for (String line : content) {
					System.out.println(">> " + line);
				}
			}
		}
	}
}
