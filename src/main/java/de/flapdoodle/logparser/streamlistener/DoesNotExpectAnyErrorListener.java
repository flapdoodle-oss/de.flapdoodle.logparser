package de.flapdoodle.logparser.streamlistener;

import de.flapdoodle.logparser.IStreamErrorListener;
import de.flapdoodle.logparser.StreamProcessException;

/**
 * TODO mmosmann: document class purpose
 * <p/>
 *
 * @author mmosmann
 */
public class DoesNotExpectAnyErrorListener implements IStreamErrorListener {
    @Override
    public void error(StreamProcessException sx) {
        throw new RuntimeException("did not expect this",sx);
    }
}
