package pitchdetection.guitar;

import pitchdetection.guitar.interfaces.GuitarTuner;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

/**
 * Implementation of guitar tuner.
 */
public class DefaultGuitarTuner implements GuitarTuner {

    /**
     * Last tune information.
     */
    private TuneInfo lastCorrectTuneInfo;

    /**
     * Frequency detector used for tuning.
     */
    private FundamentalFrequencyDetector frequencyDetector;

    @Override
    public FundamentalFrequencyDetector getFrequencyDetector() {
        return frequencyDetector;
    }

    @Override
    public void setFrequencyDetector(FundamentalFrequencyDetector frequencyDetector) {
        this.frequencyDetector = frequencyDetector;
    }

    public DefaultGuitarTuner(FundamentalFrequencyDetector frequencyDetector) {
        this.frequencyDetector = frequencyDetector;
    }

    @Override
    public TuneInfo tune() {
        float mf = this.frequencyDetector.getFundamentalFrequency();
        GuitarNote[] notes = GuitarNote.values();
        int pos = -1;
        for (int i = 0; i < notes.length; i++) {
            if (notes[i].getFrequency() > mf) {
                pos = i;
            }
        }

        if (pos == -1) {
            GuitarNote n = notes[0];
            return new TuneInfo(n, (float) n.getFrequency() - mf);
        } else if (pos == notes.length - 1) {
            GuitarNote n = notes[pos];
            return new TuneInfo(n, (float) n.getFrequency() - mf);
        } else {

            GuitarNote n = notes[pos];
            GuitarNote next = notes[pos + 1];
            if (n.getFrequency() - mf < mf - next.getFrequency()) {
                return new TuneInfo(n, (float) n.getFrequency() - mf);
            } else {
                return new TuneInfo(next, mf - (float) next.getFrequency());
            }
        }
    }

}
