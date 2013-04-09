package de.flapdoodle.logparser.matcher.generic;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.flapdoodle.logparser.GenericStreamProcessor;
import de.flapdoodle.logparser.IMatch;
import de.flapdoodle.logparser.IMatcher;
import de.flapdoodle.logparser.IReader;
import de.flapdoodle.logparser.LogEntry;
import de.flapdoodle.logparser.io.StringListReaderAdapter;
import de.flapdoodle.logparser.io.WriteToListLineProcessor;
import de.flapdoodle.logparser.matcher.stacktrace.StackTraceMatcher;
import de.flapdoodle.logparser.regex.Patterns;
import de.flapdoodle.logparser.stacktrace.StackTrace;
import de.flapdoodle.logparser.streamlistener.OnceAndOnlyOnceStreamListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * generic log matcher
 * <p/>
 *
 * @author mmosmann
 */
public class GenericLogMatcher implements IMatcher<LogEntry> {

    private final ImmutableList<Pattern> firstLinesPatterns;

    public GenericLogMatcher(Pattern firstLine, Pattern... additionalLines) {
        this.firstLinesPatterns = ImmutableList.<Pattern>builder().add(firstLine).add(additionalLines).build();
    }

    @Override
    public Optional<IMatch<LogEntry>> match(IReader reader) throws IOException {
        List<LineWithMatch> lines= Lists.newArrayList();
        for (Pattern p : firstLinesPatterns) {
            Optional<String> possibleLine = reader.nextLine();
            if (possibleLine.isPresent()) {
                Optional<Map<String, String>> match = Patterns.match(p.matcher(possibleLine.get()));
                if (match.isPresent()) {
                    lines.add(new LineWithMatch(possibleLine.get(),match.get()));
                } else {
                    return Optional.absent();
                }
            } else {
                return Optional.absent();
            }
        }
        return Optional.<IMatch<LogEntry>>of(new Match(lines));
    }

    static class LineWithMatch {
        private final String line;
        private final ImmutableMap<String, String> attributes;

        public LineWithMatch(String line, Map<String, String> attributes) {
            this.line = line;
            this.attributes = ImmutableMap.copyOf(attributes);
        }

        public String line() {
            return line;
        }

        public ImmutableMap<String, String> attributes() {
            return attributes;
        }
    }

    static class Match implements IMatch<LogEntry> {

        private final ImmutableList<LineWithMatch> lines;

        public Match(List<LineWithMatch> lines) {
            this.lines = ImmutableList.copyOf(lines);
        }

        @Override
        public LogEntry process(List<String> lines) throws IOException {
            ImmutableList.Builder<String> builder = ImmutableList.<String>builder();
            builder.addAll(Lists.transform(this.lines, new Function<LineWithMatch, String>() {
                @Override
                public String apply(LineWithMatch input) {
                    return input.line();
                }
            }));
            builder.addAll(lines);
            List<String> allLines= builder.build();

            List<Map<String, String>> attributes = Lists.transform(this.lines, new Function<LineWithMatch, Map<String, String>>() {
                @Override
                public Map<String, String> apply(LineWithMatch input) {
                    return input.attributes();
                }
            });

            Optional<StackTrace> stackTrace = Optional.absent();
            List<String> messages = ImmutableList.of();

            if (!lines.isEmpty()) {
                OnceAndOnlyOnceStreamListener<StackTrace> stackTraceListener = new OnceAndOnlyOnceStreamListener<StackTrace>();
                WriteToListLineProcessor contentListener = new WriteToListLineProcessor();

                GenericStreamProcessor<StackTrace> contentProcessor = new GenericStreamProcessor<StackTrace>(
                        Lists.<IMatcher<StackTrace>> newArrayList(new StackTraceMatcher()), contentListener, stackTraceListener);
                contentProcessor.process(new StringListReaderAdapter(lines));

                stackTrace = stackTraceListener.value();
                messages = contentListener.lines();
            }

            return new LogEntry(allLines,LogEntry.join(attributes),stackTrace,messages);
        }
    }

}
