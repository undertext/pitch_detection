package pitchdetection.sound;

import pitchdetection.sound.interfaces.AudioInputInterface;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

/**
 * Base class for all fundamental frequency detectors.
 */
abstract class FundamentalFrequencyDetectorBase implements FundamentalFrequencyDetector {

    /**
     * Number of bytes to read from audio input and pass to frequency detection algorithm.
     */
    protected int dataChunkSize;

    /**
     * Audio input to read data from.
     */
    protected AudioInputInterface audioInput;

    /**
     * Current chunk of data from audio input.
     */
    byte[] input;

    @Override
    public void setAudioInput(AudioInputInterface audioInput) {
        this.audioInput = audioInput;
    }

    @Override
    public AudioInputInterface getAudioInput() {
        return this.audioInput;
    }

    @Override
    public byte[] getCurrentData() {
        return this.input;
    }


    @Override
    public int getDataChunkSize() {
        return this.dataChunkSize;
    }

    @Override
    public void setDataChunkSize(int chunkSize) {
        this.dataChunkSize = chunkSize;
    }
}
