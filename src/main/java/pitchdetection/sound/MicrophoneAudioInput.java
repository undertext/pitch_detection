package pitchdetection.sound;

import pitchdetection.sound.interfaces.AudioInputInterface;

import javax.sound.sampled.*;

/**
 * Real microphone audion input implementation.
 */
public class MicrophoneAudioInput implements AudioInputInterface {

    /**
     * PC TargetLine to read data from.
     */
    private TargetDataLine line;

    /**
     * The bit rate of the signal.
     */
    private static final int BIT_RATE = 44100;

    /**
     * Private constructor to not allow create object with "new" keyword.
     *
     * @param line PC TargetLine to read data from.
     */
    private MicrophoneAudioInput(TargetDataLine line) {
        this.line = line;
    }

    /**
     * Fabric method to get real microphone class.
     *
     * @return RealMicrophone object
     */
    public static AudioInputInterface getAudioInput() {
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, BIT_RATE, 8, 1, 1, BIT_RATE, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Audio input not found");
        }
        TargetDataLine line = null;
        try {
            line = AudioSystem.getTargetDataLine(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return new MicrophoneAudioInput(line);
    }


    @Override
    public int getInputBitRate() {
        return BIT_RATE;
    }

    @Override
    public void startRecording() {
        try {
            this.line.open();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        this.line.start();
    }

    @Override
    public void stopRecording() {
        this.line.stop();
        this.line.flush();
    }

    @Override
    public byte[] readData(int bytesCount) {
        byte[] data = new byte[bytesCount];
        this.line.read(data, 0, bytesCount);
        return data;
    }
}
