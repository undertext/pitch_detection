package pitchdetection.sound.interfaces;

/**
 * Base interface for settings of frequency detector.
 */
public interface FrequencyDetectorSettings {

    /**
     * Set debug flag for frequency detector.
     * With debug flag enabled additional information will be outputted to console.
     *
     * @param debug Debug flag
     */
    void setDebug(boolean debug);

    /**
     * Check is debug flag enabled.
     *
     * @return Is debug flag enabled
     */
    boolean isDebug();
}
