package pitchdetection.sound.interfaces;

/**
 * Fourier based fundamental frequency detection interface.
 */
public interface FourierBasedFundamentalFrequencyDetector extends FundamentalFrequencyDetector {

    /**
     * Runs actual fourier transform.
     *
     * @return Fourier transformed data (real and imagine parts).
     */
    public float[][] runFourierTransformation();

    /**
     * Generate frequency spectrum on fourier transformed data.
     *
     * @return Frequency spectrum .
     */
    public float[] generateSpectrum();

    /**
     * Calculate spectrum picks.
     *
     * @return Spectrum picks.
     */
    public int[] calculateSpectrumPicks();

    /**
     * Calculate dominant frequencies.
     *
     * @return Dominant frequencies.
     */
    public float[] calculateDominantFrequencies();

    /**
     * Get spectrum of input data.
     *
     * @return Spectrum of input data.
     */
    public float[] getSpectrum();

    /**
     * Get local maxima indexes from spectrum array.
     *
     * @return Local maxima indexes from spectrum array.
     */
    public int[] getSpectralPicksIndexes();

}
