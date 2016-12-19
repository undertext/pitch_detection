package pitchdetection.sound.interfaces;

/**
 * Fourier based fundamental frequency detection interface.
 */
public interface FourierBasedFundamentalFrequencyDetector extends FundamentalFrequencyDetector {

    /**
     * Apply window function to the input data.
     *
     */
    void applyWindowFunction();

    /**
     * Runs actual fourier transform.
     *
     * @return Fourier transformed data (real and imagine parts).
     */
    float[][] runFourierTransformation(boolean applyWindowFunction);

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
