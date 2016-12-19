package unit.sound;


import org.junit.Assert;
import org.junit.Test;
import pitchdetection.sound.FFTFundamentalFrequencyDetector;
import pitchdetection.sound.interfaces.AudioInputInterface;
import pitchdetection.sound.interfaces.FourierBasedFundamentalFrequencyDetector;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

public class FFTFundamentalFrequencyDetectorTest {

    @Test
    public void testFFT() {
        final byte[] input = new byte[]{3, 2, 1, 0, -1, -2, -3, -4, -3, -2, -1, 0, 1, 2, 3, 4};
        float[] outputReal = new float[]{0, 24.274f, 0, 1.24f, 0, -0.553f, 0, -0.96f, 0, -0.96f, 0, -0.554f, 0, 1.24f, 0, 24.274f};
        float[] outputImaginary = new float[]{0, 10.055f, 0, 2.994f, 0, 1.336f, 0, 0.398f, 0, -0.398f, 0, -1.337f, 0, -2.994f, 0, -10.055f};

        FourierBasedFundamentalFrequencyDetector frequencyDetector = new FFTFundamentalFrequencyDetector(new AudioInputInterface() {
            @Override
            public int getInputBitRate() {
                return 0;
            }

            @Override
            public void startRecording() {

            }

            @Override
            public byte[] readData(int bytesCount) {
                return input;
            }
        }, 16);

        frequencyDetector.getAudioInput().readData(16);
        float[][] result = frequencyDetector.runFourierTransformation(false);
        Assert.assertArrayEquals(result[0], outputReal, 0.1f);
        Assert.assertArrayEquals(result[1], outputImaginary, 0.1f);
    }
}
