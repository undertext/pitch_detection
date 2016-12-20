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
    public float[] imaginary;

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
     * The fundamental frequency.
     */
    public float fundamentalFrequency;

    /**
     * Create new FFT frequency detector.
     *
     * @param audioInput Audio input to read data from.
     */
    public FFTFundamentalFrequencyDetector(AudioInputInterface audioInput, int dataChunkSize) {
        this.audioInput = audioInput;
        this.dataChunkSize = dataChunkSize;
        real = new float[dataChunkSize];
        imaginary = new float[dataChunkSize];
        spectrum = new float[dataChunkSize];
    }


    @Override
    public void applyWindowFunction() {

        for (int k = 0; k < input.length; k += 1) {

            this.real[k] = this.real[k] * (float) (0.54f - 0.46f * Math.cos(2 * Math.PI * k / (input.length - 1)));
        }
    }

    @Override
    public float[][] runFourierTransformation(boolean applyWindowFunction) {
        this.input = this.audioInput.readData(dataChunkSize);
        for (int k = 0; k < input.length; k += 1) {
            this.real[k] = this.input[k];
        }
        if (applyWindowFunction) {
            this.applyWindowFunction();
        }

        float[][] result = new float[2][input.length];
        result[0] = this.real;
        result[1] = this.imaginary;

        result = this.doFFT(result);
        this.real = result[0];
        this.imaginary = result[1];

        return new float[][]{real, imaginary};
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
            spectrum[i] = (float) Math.sqrt(real[i] * real[i] + imaginary[i] * imaginary[i]);
        }
        return spectrum;
    }

    @Override
    public int[] calculateSpectrumPicks() {
        int delta = 50;
        int lookahead = 4;
        List<Integer> maxPeaks = new ArrayList();
        boolean findMax = true;

        float maxCandidate = Float.MIN_VALUE;
        float minCandidate = Float.MAX_VALUE;
        int pos = 0;

        float[] spectrum = this.getSpectrum();

        for (int i = 0; i < spectrum.length; i++) {

            if (spectrum[i] > maxCandidate) {
                maxCandidate = spectrum[i];
                pos = i;
            }

            if (spectrum[i] < minCandidate) {
                minCandidate = spectrum[i];
            }

            if (spectrum[i] < maxCandidate - delta && findMax) {

                if (getMax(Arrays.copyOfRange(spectrum, i, i + lookahead)) < maxCandidate) {
                    if (spectrum[pos] > 50) {
                        maxPeaks.add(pos);
                    }
                    findMax = false;
                    maxCandidate = Float.MAX_VALUE;
                    minCandidate = Float.MAX_VALUE;
                    if (i + lookahead > spectrum.length) {
                        break;
                    }
                    continue;

                }
            }

            if (spectrum[i] > minCandidate + delta && !findMax) {

                if (getMin(Arrays.copyOfRange(spectrum, i, i + lookahead)) > minCandidate) {
                    findMax = true;
                    maxCandidate = Float.MIN_VALUE;
                    minCandidate = Float.MIN_VALUE;
                    if (i + lookahead > spectrum.length) {
                        break;
                    }
                    continue;

                }
            }


        }
        int[] maxPeaksArray = new int[maxPeaks.size()];

        if (maxPeaks.size() > 0 && maxPeaks.get(0) <= 5) {
            maxPeaks.remove(0);
        }

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

    private boolean isFundamental(int spectralPick, int funt, List<Integer> entry) {
        int last = entry.get(entry.size() - 1);
        if ( Math.abs(spectralPick - last - funt) <= 1 ||  Math.abs(spectralPick - last - 2 * funt ) <=  1) {
            return true;
        }

        return false;
    }

    private float calculateFundamentalFrequency() {
        Map<Integer, List<Integer>> fundamentalsMap = new LinkedHashMap();
        for (int spectralPick : spectralPicksIndexes) {
            boolean isFundamentalFound = false;
            for (int fundamental : fundamentalsMap.keySet()) {
                //   if ((float) spectralPick % fundamental <= 1 || (float) (spectralPick + 1) % fundamental <= 1) {
                if (isFundamental(spectralPick, fundamental, fundamentalsMap.get(fundamental))) {
                    fundamentalsMap.get(fundamental).add(spectralPick);
                    isFundamentalFound = true;
                    //    break;
                }
            }

            if (!isFundamentalFound) {

                if (spectralPick < 30 && spectralPick > 10) {
                    fundamentalsMap.put(spectralPick / 2 , new ArrayList());
                    fundamentalsMap.get(spectralPick / 2).add(spectralPick/2);
                    fundamentalsMap.get(spectralPick / 2).add(spectralPick);
                }

                fundamentalsMap.put(spectralPick , new ArrayList());
                fundamentalsMap.get(spectralPick).add(spectralPick);
            }
        }

        Map.Entry<Integer, List<Integer>> maxEntry = null;
        for (Map.Entry<Integer, List<Integer>> entry : fundamentalsMap.entrySet()) {
            if (maxEntry == null || entry.getValue().size() > maxEntry.getValue().size()) {
                maxEntry = entry;
            }
        }


        if (maxEntry != null && spectrum[maxEntry.getValue().get(maxEntry.getValue().size() -1)] > 90) {
            System.out.println(fundamentalsMap);
            float delta = 0;
            int pos = maxEntry.getKey();
            if (pos != -1) {
                int a = -1;
                if (this.spectrum[pos + 1] > this.spectrum[pos - 1]) {
                    a = 1;
                }
                delta = a * (this.spectrum[pos + a]) / (this.spectrum[pos + a] + this.spectrum[pos]);
            }
            return (float) (pos * this.audioInput.getInputBitRate() / this.dataChunkSize + delta * 44100f / 4096f);
        } else {
            return 0f;
        }

    }


    @Override
    public float getFundamentalFrequency() {
        this.runFourierTransformation(true);
        this.generateSpectrum();
        this.calculateSpectrumPicks();
        this.calculateDominantFrequencies();
        fundamentalFrequency = this.calculateFundamentalFrequency();
        return fundamentalFrequency;
    }

    @Override
    public float getCurrentFundamentalFrequency() {
        return this.fundamentalFrequency;
    }

    public float[] getSpectrum() {
        return spectrum;
    }

    public int[] getSpectralPicksIndexes() {
        return spectralPicksIndexes;
    }

}
