package de.flapdoodle.logparser.matcher;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;


public interface IPatternWithNames {

	String patternAsString();

	Set<String> names();

	boolean find(CharSequence input);

	Optional<Map<String, String>> parse(CharSequence input);

}
