package de.flapdoodle.logparser;

import java.io.IOException;

import com.google.common.base.Optional;

/**
 * test stream matcher
 * 
 * format:
 * message with any content
 * > comment to this message
 * 
 * @author mosmann
 *
 */
public class StreamMatcher implements IMatcher<StreamEntry> {

	@Override
	public Optional<IMatch<StreamEntry>> match(IReader reader, IBackBuffer backBuffer) throws IOException {
		Optional<String> optionalFirstLine = reader.nextLine();
		if (optionalFirstLine.isPresent()) {
			if (!optionalFirstLine.get().startsWith(">")) {
				return Optional.<IMatch<StreamEntry>>of(new StreamMatch(optionalFirstLine.get()));
			}
		}
		return Optional.absent();
	}
	
}