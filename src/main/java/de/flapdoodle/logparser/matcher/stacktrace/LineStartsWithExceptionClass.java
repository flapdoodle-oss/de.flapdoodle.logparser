package de.flapdoodle.logparser.matcher.stacktrace;

import de.flapdoodle.logparser.regex.Patterns;

import java.util.regex.Pattern;

import static de.flapdoodle.logparser.matcher.CustomPatterns.Classname;
import static de.flapdoodle.logparser.regex.Patterns.join;
import static java.util.regex.Pattern.compile;

/**
 * TODO mmosmann: document class purpose
 * <p/>
 *
 * @author mmosmann
 */
public class LineStartsWithExceptionClass {
    private static final Pattern PATTERN = join((Classname), compile(": "));

    public static boolean find(CharSequence input) {
        return Patterns.find(PATTERN, input);
    }

}
