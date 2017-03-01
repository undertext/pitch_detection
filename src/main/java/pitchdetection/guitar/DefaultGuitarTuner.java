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
    public void start() {
        this.frequencyDetector.getAudioInput().startRecording();
    }

    @Override
    public void stop() {
        this.frequencyDetector.getAudioInput().stopRecording();
    }

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
        float fundamentalFrequency = this.frequencyDetector.getFundamentalFrequency();
        if (fundamentalFrequency > 0) {
            GuitarNote[] notes = GuitarNote.values();
            int pos = -1;
            for (int i = 0; i < notes.length; i++) {
                if (notes[i].getFrequency() > fundamentalFrequency) {
                    pos = i;
                }
            }

            if (pos == -1) {
                GuitarNote closestNote = notes[0];
                lastCorrectTuneInfo = new TuneInfo(closestNote, fundamentalFrequency - (float) closestNote.getFrequency());
            } else if (pos == notes.length - 1) {
                GuitarNote closestNote = notes[pos];
                lastCorrectTuneInfo = new TuneInfo(closestNote, fundamentalFrequency - (float) closestNote.getFrequency());
            } else {
                GuitarNote leftBorderNote = notes[pos];
                GuitarNote rightBorderNote = notes[pos + 1];
                if (leftBorderNote.getFrequency() - fundamentalFrequency < fundamentalFrequency - rightBorderNote.getFrequency()) {
                    lastCorrectTuneInfo = new TuneInfo(leftBorderNote, (float) leftBorderNote.getFrequency() - fundamentalFrequency);
                } else {
                    lastCorrectTuneInfo = new TuneInfo(rightBorderNote, fundamentalFrequency - (float) rightBorderNote.getFrequency());
                }
            }
        }

        return lastCorrectTuneInfo;
    }

}
