package de.flapdoodle.logparser.matcher.javalogging;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.matcher.NamedPattern;
import de.flapdoodle.logparser.stacktrace.StackTrace;

public class StandardJavaLoggingMatcher implements IMatcher {

	List<NamedPattern> _patterns = Lists.newArrayList(new NamedPattern("start", "^"), new DatePattern(), new NamedPattern(
			"space", " "), new ClassPattern(), new NamedPattern("space", " "), new MethodPattern());

	NamedPattern _levelPattern=new LevelPattern();
	
	@Override
	public boolean match(String firstLine) {
		int currentPosition = 0;

		for (NamedPattern p : _patterns) {
			Matcher matcher = p.matcher(firstLine);
			if (matcher.find(currentPosition)) {
//				System.out.println(p.name() + "=" + matcher.group());
				currentPosition = matcher.end();
			} else {
				return false;
			}
		}
//		if (firstLine.length()>currentPosition) {
//			System.out.println("left=" + firstLine.substring(currentPosition));
//		}

		return true;
	}

	@Override
	public void process(List<String> lines) throws IOException {
		if (lines.size()>1) {
			String firstLine=lines.get(0);
			String secondLine=lines.get(1);
			System.out.println("> "+firstLine);
			System.out.println("> "+secondLine);
			
			List<String> content = lines.subList(2, lines.size());
			
			Optional<StackTrace> stacktrace = StackTraceParser.parse(content);
			if (stacktrace.isPresent()) {
				System.out.println(">> stacktrace found");
			} else {
				
				for (String line : content) {
					System.out.println(">> "+line);
				}
			}
		}
	}
}
