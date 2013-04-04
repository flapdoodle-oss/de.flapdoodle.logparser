package de.flapdoodle.logparser;

import java.io.IOException;

import com.google.common.base.Optional;

public interface IReader{
	Optional<String> nextLine() throws IOException;

	void setMarker() throws IOException;

	void jumpToMarker() throws IOException;
}