package pitchdetection.sound.interfaces;

/**
 * Fourier based fundamental frequency detection interface.
 */
public interface FourierBasedFundamentalFrequencyDetector extends FundamentalFrequencyDetector {

    /**
     * Apply window function to the input data.
     *
     * @param data Input data.
     */
    void applyWindowFunction(byte[] data);

    /**
     * Runs actual fourier transform.
     *
     * @return Fourier transformed data (real and imagine parts).
     */
    float[][] runFourierTransformation();

    /**
     * Generate frequency spectrum on fourier transformed data.
     *
     * @return Frequency spectrum .
     */
    float[] generateSpectrum();

    /**
     * Calculate spectrum picks.
     *
     * @return Spectrum picks.
     */
    int[] calculateSpectrumPicks();

    /**
     * Calculate dominant frequencies.
     *
     * @return Dominant frequencies.
     */
    float[] calculateDominantFrequencies();

    /**
     * Get spectrum of input data.
     *
     * @return Spectrum of input data.
     */
    float[] getSpectrum();

    /**
     * Get local maxima indexes from spectrum array.
     *
     * @return Local maxima indexes from spectrum array.
     */
    int[] getSpectralPicksIndexes();

}
