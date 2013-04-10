package de.flapdoodle.logparser;

import com.google.common.base.Optional;

/**
 * TODO mmosmann: document class purpose
 * <p/>
 *
 * @author mmosmann
 */
public class StreamProcessException extends Exception {

    private final Object entry;

    public StreamProcessException(String message,Object entry, Throwable cause) {
        super(message, cause);
        this.entry = entry;
    }

    public <T> Optional<T> entry(Class<T> type) {
        if (entry!=null) {
            if (type.isInstance(entry)) {
                return Optional.of((T)entry);
            }
        }
        return Optional.absent();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + entry;
    }
}
