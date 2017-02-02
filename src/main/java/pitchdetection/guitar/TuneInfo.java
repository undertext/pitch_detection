package pitchdetection.guitar;

import java.util.Locale;

/**
 * Contains real-time tune information.
 */
public class TuneInfo {

    /**
     * Detected note.
     */
    public GuitarNote note;

    /**
     * Deviation of frequency compared to detected note frequency.
     */
    public float delta;

    /**
     * TuneInfo constructor.
     * @param note Detected note
     * @param delta Deviation of frequency compared to detected note frequency.
     */
    public TuneInfo(GuitarNote note, float delta) {
        this.note = note;
        this.delta = delta;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s %+.2f", note.name(), delta);
    }
}
