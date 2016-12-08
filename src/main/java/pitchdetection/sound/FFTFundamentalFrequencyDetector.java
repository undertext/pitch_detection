package pitchdetection.sound;


import pitchdetection.sound.interfaces.AudioInputInterface;
import pitchdetection.sound.interfaces.FourierBasedFundamentalFrequencyDetector;

import java.util.*;

/**
 * Frequency detector based on Fast Fourier Transform.
 */
public class FFTFundamentalFrequencyDetector extends FundamentalFrequencyDetectorBase implements FourierBasedFundamentalFrequencyDetector {

    /**
     * Real part of FFT result.
     */
    public float[] real;

    /**
     * Imaginary part of FFT result.
     */
    public float[] imag;

    /**
     * Spectrum result.
     */
    public float[] spectrum;

    /**
     * Indexes of picks in spectrum array.
     */
    public int[] spectralPicksIndexes;

    /**
     * Dominant frequencies in sound input.
     */
    public float[] dominantFrequencies;

    /**
     * Create new FFT frequency detector.
     *
     * @param audioInput Audio input to read data from.
     */
    public FFTFundamentalFrequencyDetector(AudioInputInterface audioInput, int dataChunkSize) {
        this.audioInput = audioInput;
        this.dataChunkSize = dataChunkSize;
        real = new float[dataChunkSize];
        imag = new float[dataChunkSize];
        spectrum = new float[dataChunkSize];
    }


    @Override
    public float[][] runFourierTransformation() {

        this.input = this.audioInput.readData(dataChunkSize);

        for (int k = 0; k < input.length; k += 1) {
            this.real[k] = this.input[k];
        }

        float[][] res = new float[2][input.length];
        res[0] = this.real;
        res[1] = this.imag;

        res = this.doFFT(res);
        this.real = res[0];
        this.imag = res[1];

        return new float[][]{real, imag};
    }

    /**
     * Recursion implementation of FFT algorithm.
     *
     * @param input Input data
     * @return Fourier transformed data.
     */
    public float[][] doFFT(float[][] input) {

        int N = input[0].length;
        float output[][] = new float[2][N];

        if (N == 1) {
            output[0][0] = input[0][0];
            output[1][0] = 0;
        } else {

            // fft of even terms
            float[][] even = new float[2][N / 2];
            for (int k = 0; k < N / 2; k++) {
                even[0][k] = input[0][2 * k];
                even[1][k] = input[1][2 * k];
            }
            float[][] q = doFFT(even);

            // fft of even terms
            float[][] odd = new float[2][N / 2];
            for (int k = 0; k < N / 2; k++) {
                odd[0][k] = input[0][2 * k + 1];
                odd[1][k] = input[0][2 * k + 1];
            }

            float[][] r = doFFT(odd);

            for (int k = 0; k < N / 2; k += 1) {

                float cosOffset = (float) Math.cos(-2 * Math.PI * k / N);
                float sinOffset = (float) Math.sin(-2 * Math.PI * k / N);


                output[0][k] = (q[0][k] + cosOffset * r[0][k] - sinOffset * r[1][k]);
                output[1][k] = (q[1][k] + cosOffset * r[1][k] + sinOffset * r[0][k]);

                output[0][k + N / 2] = (q[0][k] - cosOffset * r[0][k] + sinOffset * r[1][k]);
                output[1][k + N / 2] = (q[1][k] - cosOffset * r[1][k] - sinOffset * r[0][k]);

            }
        }

        return output;
    }

    @Override
    public float[] generateSpectrum() {
        for (int i = 0; i < spectrum.length; i++) {
            spectrum[i] = (float) Math.sqrt(real[i] * real[i] + imag[i] * imag[i]);
        }
        return spectrum;
    }

    @Override
    public int[] calculateSpectrumPicks() {
        int delta = 20;
        int lookahead = 10;
        List<Integer> maxPeaks = new ArrayList<Integer>();
        boolean findMax = true;

        float maxCandidate = Float.MIN_VALUE;
        float minCandidate = Float.MAX_VALUE;
        int pos = 0;


        for (int i = 0; i < this.spectrum.length; i++) {

            if (this.spectrum[i] > maxCandidate) {
                maxCandidate = this.spectrum[i];
                pos = i;
            }

            if (this.spectrum[i] < minCandidate) {
                minCandidate = this.spectrum[i];
            }

            if (this.spectrum[i] < maxCandidate - delta && findMax) {

                if (getMax(Arrays.copyOfRange(this.spectrum, i, i + lookahead)) < maxCandidate) {
                    maxPeaks.add(pos);
                    findMax = false;
                    maxCandidate = Float.MAX_VALUE;
                    minCandidate = Float.MAX_VALUE;
                    if (i + lookahead > this.spectrum.length) {
                        break;
                    }
                    continue;

                }
            }

            if (this.spectrum[i] > minCandidate + delta && !findMax) {

                if (getMin(Arrays.copyOfRange(this.spectrum, i, i + lookahead)) > minCandidate) {
                    findMax = true;
                    maxCandidate = Float.MIN_VALUE;
                    minCandidate = Float.MIN_VALUE;
                    if (i + lookahead > this.spectrum.length) {
                        break;
                    }
                    continue;

                }
            }


        }
        int[] maxPeaksArray = new int[maxPeaks.size()];
        for (int i = 0; i < maxPeaks.size(); i++) {
            maxPeaksArray[i] = maxPeaks.get(i);
        }
        this.spectralPicksIndexes = maxPeaksArray;
        return maxPeaksArray;
    }

    public float[] calculateDominantFrequencies() {
        int length = this.spectralPicksIndexes.length;
        float[] dominantFrequencies = new float[length];

        for (int i = 0; i < spectralPicksIndexes.length; i++) {
            dominantFrequencies[i] = (float) spectralPicksIndexes[i] * this.audioInput.getInputBitRate() / this.dataChunkSize;
        }

        this.dominantFrequencies = dominantFrequencies;

        return dominantFrequencies;
    }


    @Override
    public byte[] getCurrentData() {
        return this.input;
    }


    private float getMax(float[] data) {
        float max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    private float getMin(float[] data) {
        float min = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] < min) {
                min = data[i];
            }
        }
        return min;
    }

    private float calculateFundamentalFrequency() {
        Map<Integer, Integer> some = new LinkedHashMap<Integer, Integer>();
        for (int spectralPick : spectralPicksIndexes) {
            boolean b = false;
            for (int f : some.keySet()) {
                if ((float) spectralPick % f <= 1 || (float) (spectralPick + 1) % f <= 1) {
                    some.put(f, some.get(f) + 1);
                    b = true;
                    break;
                }
            }
            if (!b) {
                some.put(spectralPick, 1);
            }
        }


        Map.Entry<Integer, Integer> maxEntry = null;
        System.out.println(some);
        for (Map.Entry<Integer, Integer> entry : some.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        if (maxEntry != null) {
            float delta = 0;
            int pos = maxEntry.getKey();
            if (pos != -1) {
                int a = -1;
                if (this.spectrum[pos + 1] > this.spectrum[pos - 1]) {
                    a = 1;
                }
                delta = a * (this.spectrum[pos + a]) / (this.spectrum[pos + a] + this.spectrum[pos]);
            }
            return (float) pos * this.audioInput.getInputBitRate() / this.dataChunkSize + delta;
        } else {
            return 0f;
        }

    }


    @Override
    public float getFundamentalFrequency() {
        this.runFourierTransformation();
        this.generateSpectrum();
        this.calculateSpectrumPicks();
        this.calculateDominantFrequencies();
        return this.calculateFundamentalFrequency();
    }

    public float[] getSpectrum() {
        return spectrum;
    }

    public int[] getSpectralPicksIndexes() {
        return spectralPicksIndexes;
    }

}
