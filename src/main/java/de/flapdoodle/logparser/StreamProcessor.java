/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *   ${lic.username2} <${lic.email2}>
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

public final class StreamProcessor {

	private final Collection<IMatcher> _matcher;
	private final ILineProcessor _defaultLineProcessor;

	public StreamProcessor(Collection<IMatcher> matcher,ILineProcessor defaultLineProcessor) {
		_matcher = matcher;
		_defaultLineProcessor = defaultLineProcessor;
	}

	public void process(IReader reader) throws IOException {
		boolean eof = false;
		do {
			Optional<String> optLine = reader.nextLine();
			if (optLine.isPresent()) {

				String line = optLine.get();
				Optional<IMatcher> match = firstMatch(line);
				if (match.isPresent()) {
					
					List<String> allLines=Lists.newArrayList(line);
					
					boolean readAheadDone=false;
					do {
						reader.setMarker();
						Optional<String> readAheadLine = reader.nextLine();
						if (readAheadLine.isPresent()) {
							Optional<IMatcher> readAheadMatch = firstMatch(readAheadLine.get());
							if (!readAheadMatch.isPresent()) {
								allLines.add(readAheadLine.get());
							} else {
								reader.jumpToMarker();
								readAheadDone=true;
							}
						} else {
							readAheadDone=true;
						}
					} while (!readAheadDone);
					
					match.get().process(allLines);
					
				} else {
					_defaultLineProcessor.processLine(line);
				}
			} else {
				eof = true;
			}
		} while (!eof);
	}

	private Optional<IMatcher> firstMatch(String line) {
		for (IMatcher matcher : _matcher) {
			if (matcher.match(line)) {
				return Optional.of(matcher);
			}
		}
		return Optional.absent();
	}
}
