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
package de.flapdoodle.logparser.matcher.stacktrace;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.flapdoodle.logparser.IBackBuffer;
import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.stacktrace.ExceptionAndMessage;
import de.flapdoodle.logparser.stacktrace.StackTrace;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class StackTraceMatcher implements IMatcher<StackTrace> {

	static final Logger _logger = Logger.getLogger(StackTraceMatcher.class.getName());

	static final int MAX_LOOK_AHEAD = 30;

	@Override
	public Optional<IMatch<StackTrace>> match(IReader reader, IBackBuffer backBuffer) throws IOException {
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
			if (causeByButNothingElseAtIn(backBuffer.lastLines())) {
				return Optional.absent();
			}

			Optional<FirstLine> firstLine = FirstLine.match(firstLineAsString);
			if (firstLine.isPresent()) {

				int leftToTry = MAX_LOOK_AHEAD;
				List<String> messageLines = Lists.newArrayList();

				do {
					leftToTry--;

					Optional<String> possibleSecondLine = reader.nextLine();
					if (possibleSecondLine.isPresent()) {
						Optional<At> secondLine = At.match(possibleSecondLine.get());
						if (secondLine.isPresent()) {
							return Optional.<IMatch<StackTrace>> of(new StackTraceMatch(firstLine.get(), messageLines,
									secondLine.get()));
						} else {
							messageLines.add(possibleSecondLine.get());
						}
					} else {
						leftToTry = 0;
					}
				} while (leftToTry > 0);

			}
		}
		return Optional.absent();
	}

	private boolean causeByButNothingElseAtIn(ImmutableList<String> lastLines) {
		for (String line : lastLines) {
			if (CauseBy.find(line)) {
				return true;
			}
			if (At.find(line)) {
				return false;
			}
			if (More.find(line)) {
				return false;
			}
		}
		return false;
	}

	static class StackTraceMatch implements IMatch<StackTrace> {

		private Stack _stack;
		private StackLines _stackLines;

		public StackTraceMatch(FirstLine firstLine, List<String> messages, At at) {
			_stack = new Stack(firstLine, messages);
			_stackLines = new StackLines();
			_stackLines.add(at);
			_stack.addStackLines(_stackLines);
		}

		@Override
		public StackTrace process(List<String> lines) throws IOException {

			IStackContainer stack = _stack;
			StackLines stackLines = _stackLines;

			boolean lastOneWasCauseBy = false;

			for (String line : lines) {
				Optional<At> at = At.match(line);
				if (at.isPresent()) {
					lastOneWasCauseBy = false;

					stackLines.add(at.get());
				} else {
					Optional<CauseBy> causeBy = CauseBy.match(line);
					if (causeBy.isPresent()) {
						lastOneWasCauseBy = true;

						stack = stack.causeBy(causeBy.get());
						stackLines = new StackLines();
						stack.addStackLines(stackLines);
					} else {
						Optional<More> more = More.match(line);
						if (more.isPresent()) {
							lastOneWasCauseBy = false;

							stackLines.more(more.get());
							stackLines = new StackLines();
							stack.addStackLines(stackLines);
						} else {
							if (!line.trim().isEmpty()) {
								if (!lastOneWasCauseBy) {
									_logger.fine("unknown type of line: '" + line + "'");
								}
								stack.addMessage(line);
							}
						}
					}
				}
			}

			return toStackTrace(lines, _stack);
		}
	}

	private static StackTrace toStackTrace(List<String> source, Stack stack) {
		return new StackTrace(source, exceptionAndMessage(stack._firstLine, stack.messages()),
				stackLines(stack.stackLines()), cause(stack.causeBy()));
	}

	private static de.flapdoodle.logparser.stacktrace.CauseBy cause(CauseByStack causeBy) {
		if (causeBy != null) {
			return new de.flapdoodle.logparser.stacktrace.CauseBy(exceptionAndMessage(causeBy._cause, causeBy.messages()),
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

	private static Collection<de.flapdoodle.logparser.stacktrace.StackLines> stackLines(Collection<StackLines> atLines) {
		Collection<StackLines> filtered = Collections2.filter(atLines, new Predicate<StackLines>() {
			@Override
			public boolean apply(StackLines stackLine) {
				return !stackLine.atLines().isEmpty();
			}
		});
		return Collections2.transform(filtered, new Function<StackLines, de.flapdoodle.logparser.stacktrace.StackLines>() {

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

	private static ExceptionAndMessage exceptionAndMessage(CauseBy causeBy, List<String> messages) {
		return new ExceptionAndMessage(causeBy.exception(), ImmutableList.<String> builder().add(causeBy.message()).addAll(
				messages).build());
	}

	private static ExceptionAndMessage exceptionAndMessage(FirstLine firstLine, List<String> messages) {
		return new ExceptionAndMessage(firstLine.exception(),
				ImmutableList.<String> builder().add(firstLine.message()).addAll(messages).build());
	}

	static interface IStackLines {

		void add(At at);

		void more(More more);

	}

	static interface IStackContainer {

//		IStackLines newStackLines();

		IStackContainer causeBy(CauseBy causeBy);

//		IStackLines currentStackLines();
		void addStackLines(StackLines stackLines);

		void addMessage(String line);
	}

	static abstract class AbstractStack implements IStackContainer {

		private final List<StackLines> _stackLines = Lists.newArrayList();
		private CauseByStack _causeBy;
		private final List<String> _messages = Lists.newArrayList();

		@Override
		public IStackContainer causeBy(CauseBy causeBy) {
			_causeBy = new CauseByStack(causeBy);
			return _causeBy;
		}

//		@Override
//		public IStackLines currentStackLines() {
//			return _stackLines.get(_stackLines.size() - 1);
//		}

//		public IStackLines newStackLines() {
//			StackLines ret = new StackLines();
//			_stackLines.add(ret);
//			return ret;
//		}
		public void addStackLines(StackLines stackLines) {
			_stackLines.add(stackLines);
		}


		public ImmutableList<StackLines> stackLines() {
			return ImmutableList.copyOf(_stackLines);
		}

		public CauseByStack causeBy() {
			return _causeBy;
		}

		@Override
		public void addMessage(String line) {
			_messages.add(line);
		}

		public void addMessages(List<String> messages) {
			_messages.addAll(messages);
		}

		public ImmutableList<String> messages() {
			return ImmutableList.copyOf(_messages);
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

		public ImmutableList<At> atLines() {
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

		Stack(FirstLine firstLine, List<String> messages) {
			_firstLine = firstLine;
			addMessages(messages);
		}

	}

	static class CauseByStack extends AbstractStack {

		private final CauseBy _cause;

		public CauseByStack(CauseBy cause) {
			_cause = cause;
		}
	}
}
