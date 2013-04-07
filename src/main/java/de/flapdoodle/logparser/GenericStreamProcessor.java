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

	public void process(IReader reader) throws IOException {
		Optional<String> possibleLine = reader.nextLine();
		if (possibleLine.isPresent()) {
			String line = possibleLine.get();

			Optional<IMatch> match = firstMatch(line);
			if (match.isPresent()) {
				process(reader, line, match);
			} else {
				_defaultLineProcessor.processLine(line);
			}
		}
	}

	private void process(IReader reader, String line, Optional<IMatch> match) throws IOException {
		List<String> nonMatchingLines = Lists.newArrayList();

		boolean readDone=false;
		
		do {
			reader.setMarker();
			Optional<String> nextLine = reader.nextLine();
			if (nextLine.isPresent()) {
				Optional<IMatch> nextMatch = firstMatch(nextLine.get());
				if (nextMatch.isPresent()) {
					match.get().process(nonMatchingLines);
					nonMatchingLines=Lists.newArrayList();
				} else {
					nonMatchingLines.add(nextLine.get());
				}
			} else {
				readDone=true;
			}
		} while (!readDone);
	}

	Optional<IMatch> firstMatch(String line) {
		for (IMatcher matcher : _matcher) {
			Optional<IMatch> match = matcher.match(line);
			if (match.isPresent()) {
				return match;
			}
		}
		return Optional.absent();
	}

}
