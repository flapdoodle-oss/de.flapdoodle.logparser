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
package de.flapdoodle.logparser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class GenericStreamProcessor {

	private final Collection<IMatcher> _matcher;
	private final ILineProcessor _defaultLineProcessor;

	public GenericStreamProcessor(Collection<IMatcher> matcher, ILineProcessor defaultLineProcessor) {
		_matcher = matcher;
		_defaultLineProcessor = defaultLineProcessor;
	}

	public void process(IRewindableReader reader) throws IOException {
		boolean eof = false;

		do {
			Optional<IMatch> match = firstMatch(reader);
			if (match.isPresent()) {
				process(reader, match);
			} else {
				Optional<String> nextLine = reader.nextLine();
				if (nextLine.isPresent()) {
					_defaultLineProcessor.processLine(nextLine.get());
				} else {
					eof = true;
				}
			}
		} while (!eof);
	}

	private void process(IRewindableReader reader, Optional<IMatch> match) throws IOException {
		List<String> nonMatchingLines = Lists.newArrayList();

		boolean readDone = false;

		do {
			Optional<IMatch> nextMatch = firstMatch(reader);
			if (nextMatch.isPresent()) {
				match.get().process(nonMatchingLines);
				nonMatchingLines = Lists.newArrayList();
			} else {
				Optional<String> nextLine = reader.nextLine();
				if (nextLine.isPresent()) {
					nonMatchingLines.add(nextLine.get());
				} else {
					readDone = true;
				}
			}
		} while (!readDone);
	}

	private Optional<IMatch> firstMatch(IRewindableReader reader) throws IOException {
		reader.setMarker();
		for (IMatcher matcher : _matcher) {
			Optional<IMatch> match = matcher.match(reader);
			if (match.isPresent()) {
				return match;
			} else {
				reader.jumpToMarker();
			}
		}
		return Optional.absent();
	}
}
