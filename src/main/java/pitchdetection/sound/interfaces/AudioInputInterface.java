package pitchdetection.sound.interfaces;

/**
 * Wraps Audio input functionality in reusable interface.
 */
public interface AudioInputInterface {

    /**
     * Get audio input bit rate.
     *
     * @return Microphone input bit rate.
     */
    int getInputBitRate();

    /**
     * Open audio input line and start recording data.
     */
    void startRecording();

    /**
     * Close audio input line and stop recording data.
     */
    void stopRecording();

    /**
     * Read given count of bytes from audio input line.
     *
     * @param bytesCount Count of bytes to read from audio input line.
     * @return Data from audio input line.
     */
    byte[] readData(int bytesCount);

}
