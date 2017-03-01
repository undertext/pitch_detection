package pitchdetection.sound;

import pitchdetection.sound.interfaces.AudioInputInterface;

/**
 * Class for simulating sound input.
 * <p>
 * Used for testing reasons.
 * @todo Do we actually need this?
 */
public class FakeAudioInput implements AudioInputInterface {

    /**
     * Frequency of generated sound wave.
     */
    private static int currentFrequency;

    /**
     * The bit rate of fake signal.
     */
    private static final int BIT_RATE = 44100;

    /**
     * The default frequency of fake signal.
     */
    private static final int SOUND_FREQUENCY = 600;

    /**
     * FakeAudioInput constructor.
     *
     * @param frequency Frequency of fake signal.
     */
    public FakeAudioInput(int frequency) {
        this.currentFrequency = frequency;
    }

    /**
     * Fabric method to get our fake audio input.
     *
     * @return FakeMicrophone object
     */
    public static AudioInputInterface getAudioInput() {
        return new FakeAudioInput(SOUND_FREQUENCY);
    }

    @Override
    public int getInputBitRate() {
        return BIT_RATE;
    }

    @Override
    public void startRecording() {

    }

    @Override
    public void stopRecording() {

    }

    @Override
    public byte[] readData(int bytesCount) {
        byte[] output = new byte[bytesCount];
        double amplitude = 2 * Math.PI * currentFrequency;
        for (int i = 0; i < output.length; i++) {
            output[i] = (byte) (Math.sin(amplitude * i / BIT_RATE) * 60);
        }
        return output;
    }
}
