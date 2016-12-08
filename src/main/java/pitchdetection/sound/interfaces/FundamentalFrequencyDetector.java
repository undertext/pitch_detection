package pitchdetection.sound.interfaces;

/**
 * Fundamental frequency detection abstraction.
 */
public interface FundamentalFrequencyDetector {

    /**
     * Set audio input to read data from.
     *
     * @param audioInput Audio input to read data from.
     */
    void setAudioInput(AudioInputInterface audioInput);

    /**
     * Get current audio input.
     *
     * @return Current audio input.
     */
    AudioInputInterface getAudioInput();

    /**
     * Get size of data chunk - number of bytes to read from audion input
     * for each iteration of frequency detection algorithm.
     *
     * @return Size of data chunk
     */
    int getDataChunkSize();

    /**
     * Set size of data chunk - number of bytes to read from audion input
     * for each iteration of frequency detection algorithm.
     *
     * @param chunkSize Size of data chunk
     */
    void setDataChunkSize(int chunkSize);

    /**
     * Get current data used for frequency detection.
     *
     * @return Current data used for frequency detection.
     */
    byte[] getCurrentData();

    /**
     * Get fundamental frequency of input data/signal.
     *
     * @return Fundamental frequency of input data/signal.
     */
    float getFundamentalFrequency();

}
