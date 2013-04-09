/**
 * Copyright (C) 2013
 * Michael Mosmann <michael@mosmann.de>
 * 
 * with contributions from
 * ${lic.developers}
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.logparser.matcher.stacktrace;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.stacktrace.ExceptionAndMessage;
import de.flapdoodle.logparser.stacktrace.StackTrace;

import java.io.IOException;
import java.util.List;

public class StackTraceMatcher implements IMatcher<StackTrace> {

	@Override
	public Optional<IMatch<StackTrace>> match(IReader reader) throws IOException {
		Optional<String> lastLine = reader.lastLine();
		if (lastLine.isPresent()) {
			if (CauseBy.find(lastLine.get())) {
				return Optional.absent();
			}
		}

		Optional<String> possibleFirstLine = reader.nextLine();
		if (possibleFirstLine.isPresent()) {
			String firstLineAsString = possibleFirstLine.get();
			if (CauseBy.find(firstLineAsString)) {
				return Optional.absent();
			}
			if (At.find(firstLineAsString)) {
				return Optional.absent();
			}
			if (More.find(firstLineAsString)) {
				return Optional.absent();
			}

			Optional<String> possibleSecondLine = reader.nextLine();
			if (possibleSecondLine.isPresent()) {
				Optional<At> secondLine = At.match(possibleSecondLine.get());
				if (secondLine.isPresent()) {
					Optional<FirstLine> firstLine = FirstLine.match(firstLineAsString);
					if (firstLine.isPresent()) {
						return Optional.<IMatch<StackTrace>> of(new StackTraceMatch(firstLine.get(), secondLine.get()));
					}
				}
			}
		}
		return Optional.absent();
	}

	static class StackTraceMatch implements IMatch<StackTrace> {

		private Stack _stack;

		public StackTraceMatch(FirstLine firstLine, At at) {
			_stack = new Stack(firstLine, at);
		}

		@Override
		public StackTrace process(List<String> lines) throws IOException {

			IStackContainer stack = _stack;
			IStackLines stackLines = stack.currentStackLines();

			for (String line : lines) {
				Optional<At> at = At.match(line);
				if (at.isPresent()) {
					stackLines.add(at.get());
				} else {
					Optional<CauseBy> causeBy = CauseBy.match(line);
					if (causeBy.isPresent()) {
						stack = stack.causeBy(causeBy.get());
						stackLines = stack.currentStackLines();
					} else {
						Optional<More> more = More.match(line);
						if (more.isPresent()) {
							stackLines.more(more.get());
							stackLines = stack.newStackLines();
						}
					}
				}
			}

			return toStackTrace(lines, _stack);
		}
	}

	private static StackTrace toStackTrace(List<String> source, Stack stack) {
		return new StackTrace(source, exceptionAndMessage(stack._firstLine), stackLines(stack.stackLines()),
				cause(stack.causeBy()));
	}

	private static de.flapdoodle.logparser.stacktrace.CauseBy cause(CauseByStack causeBy) {
		if (causeBy != null) {
			return new de.flapdoodle.logparser.stacktrace.CauseBy(exceptionAndMessage(causeBy._cause),
					stackLines(causeBy.stackLines()), cause(causeBy.causeBy()));
		}
		return null;
	}

	private static de.flapdoodle.logparser.stacktrace.More more(More more) {
		if (more != null) {
			return new de.flapdoodle.logparser.stacktrace.More(more.count());
		}
		return null;
	}

	private static List<de.flapdoodle.logparser.stacktrace.StackLines> stackLines(List<StackLines> atLines) {
		return Lists.transform(atLines, new Function<StackLines, de.flapdoodle.logparser.stacktrace.StackLines>() {

			@Override
			public de.flapdoodle.logparser.stacktrace.StackLines apply(StackLines at) {
				return new de.flapdoodle.logparser.stacktrace.StackLines(at(at.atLines()), more(at.more()));
			}
		});
	}

	private static List<de.flapdoodle.logparser.stacktrace.At> at(List<At> atLines) {
		return Lists.transform(atLines, new Function<At, de.flapdoodle.logparser.stacktrace.At>() {

			@Override
			public de.flapdoodle.logparser.stacktrace.At apply(At at) {
				return new de.flapdoodle.logparser.stacktrace.At(at.classname(), at.method(), at.file(), at.lineNr());
			}
		});
	}

	private static ExceptionAndMessage exceptionAndMessage(CauseBy causeBy) {
		return new ExceptionAndMessage(causeBy.exception(), causeBy.message());
	}

	private static ExceptionAndMessage exceptionAndMessage(FirstLine firstLine) {
		return new ExceptionAndMessage(firstLine.exception(), firstLine.message());
	}

	static interface IStackLines {

		void add(At at);

		void more(More more);

	}

	static interface IStackContainer {

		IStackLines newStackLines();

		IStackContainer causeBy(CauseBy causeBy);

		IStackLines currentStackLines();
	}

	static abstract class AbstractStack implements IStackContainer {

		private final List<StackLines> _stackLines = Lists.newArrayList(new StackLines());
		private CauseByStack _causeBy;

		@Override
		public IStackContainer causeBy(CauseBy causeBy) {
			_causeBy = new CauseByStack(causeBy);
			return _causeBy;
		}

		@Override
		public IStackLines currentStackLines() {
			return _stackLines.get(_stackLines.size() - 1);
		}

		public IStackLines newStackLines() {
			StackLines ret = new StackLines();
			_stackLines.add(ret);
			return ret;
		}

		public List<StackLines> stackLines() {
			return ImmutableList.copyOf(_stackLines);
		}

		public CauseByStack causeBy() {
			return _causeBy;
		}

	}

	static class StackLines implements IStackLines {

		private final List<At> _atLines = Lists.newArrayList();
		private More _more;

		public void add(At at) {
			if (_more != null) {
				throw new RuntimeException("more (" + _more.line() + ") allready set for this stackframe, at=" + at.line());
			}
			_atLines.add(at);
		}

		public List<At> atLines() {
			return ImmutableList.copyOf(_atLines);
		}

		public void more(More more) {
			_more = more;
		}

		public More more() {
			return _more;
		}
	}

	static class Stack extends AbstractStack {

		private final FirstLine _firstLine;

		Stack(FirstLine firstLine, At at) {
			_firstLine = firstLine;
			currentStackLines().add(at);
		}

	}

	static class CauseByStack extends AbstractStack {

		private final CauseBy _cause;

		public CauseByStack(CauseBy cause) {
			_cause = cause;
		}
	}
}
