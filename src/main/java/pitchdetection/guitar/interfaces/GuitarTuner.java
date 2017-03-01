package pitchdetection.guitar.interfaces;

import pitchdetection.guitar.TuneInfo;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

/**
 * Guitar tuner interface.
 */
public interface GuitarTuner {

    /**
     * Start the tuner.
     */
    public void start();

    /**
     * Stop the tuner.
     */
    public void stop();

    /**
     * Get frequency detector.
     *
     * @return Frequency detector.
     */
    FundamentalFrequencyDetector getFrequencyDetector();

    /**
     * Set Frequency detector.
     *
     * @param frequencyDetector Frequency detector.
     */
    void setFrequencyDetector(FundamentalFrequencyDetector frequencyDetector);

    /**
     * Get real-time tune information.
     *
     * @return Real-time tune information.
     */
    public TuneInfo tune();

}
