package de.flapdoodle.logparser;

/**
 * stream error
 * <p/>
 *
 * @author mmosmann
 */
public interface IStreamErrorListener {
    void error(StreamProcessException sx);
}
