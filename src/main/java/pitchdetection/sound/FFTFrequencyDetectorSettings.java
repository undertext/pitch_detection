package pitchdetection.sound;


import pitchdetection.sound.interfaces.FrequencyDetectorSettings;

/**
 * Settings class for FFT frequency detector.
 */
public class FFTFrequencyDetectorSettings implements FrequencyDetectorSettings {

    /**
     * Count of items in spectrum array to observe.
     */
    private int spectrumObservablesCount = 300;

    /**
     * Debug flag.
     */
    private boolean isDebug = false;

    /**
     * Spectrum pick delta.
     */
    private int spectrumPickDelta = 30;

    /**
     * Spectrum pick look ahead.
     */
    private int spectrumPickLookAhead = 3;

    /**
     * Base note max index.
     */
    private int baseNoteMaxIndex = 200;

    /**
     * Spectrum pick minimum amplitude.
     */
    private int spectrumPickMinAmplitude = 40;

    /**
     * Overtones minimum count.
     */
    private int overtoneMinCount = 4;


    public int getOvertoneMinCount() {
        return overtoneMinCount;
    }

    @Override
    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    public void setOvertoneMinCount(int overtoneMinCount) {
        this.overtoneMinCount = overtoneMinCount;
    }

    public int getSpectrumObservablesCount() {
        return spectrumObservablesCount;
    }

    public int getSpectrumPickDelta() {
        return spectrumPickDelta;
    }

    public void setSpectrumPickDelta(int spectrumPickDelta) {
        this.spectrumPickDelta = spectrumPickDelta;
    }

    public void setSpectrumObservablesCount(int spectrumObservablesCount) {
        this.spectrumObservablesCount = spectrumObservablesCount;
    }


    public int getSpectrumPickLookAhead() {
        return spectrumPickLookAhead;
    }

    public void setSpectrumPickLookAhead(int spectrumPickLookAhead) {
        this.spectrumPickLookAhead = spectrumPickLookAhead;
    }

    public int getBaseNoteMaxIndex() {
        return baseNoteMaxIndex;
    }

    public void setBaseNoteMaxIndex(int baseNoteMaxIndex) {
        this.baseNoteMaxIndex = baseNoteMaxIndex;
    }

    public int getSpectrumPickMinAmplitude() {
        return spectrumPickMinAmplitude;
    }

    public void setSpectrumPickMinAmplitude(int spectrumPickMinAmplitude) {
        this.spectrumPickMinAmplitude = spectrumPickMinAmplitude;
    }
}
