package pitchdetection.guitar;

/**
 * Guitar notes enumeration for 6 string guitar in standard tuning.
 *
 * @author Yaroslav Kharchenko
 * @todo As we have unlimited number of notes we should get frequency of note by Name pragmatically.
 * @see <a href = http://www.phy.mtu.edu/~suits/NoteFreqCalcs.html> Example </a>
 */
public enum GuitarNote {
    E4(329.63),
    B3(246.94),
    G3(196.00),
    D3(146.83),
    A2(110.00),
    E2(82.41);

    /**
     * Frequency of the note.
     */
    private final double frequency;

    /**
     * Get frequency of the note.
     *
     * @return Frequency of the note.
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * Constructor of enumeration
     *
     * @param frequency Frequency of the note.
     */
    private GuitarNote(double frequency) {
        this.frequency = frequency;
    }
}
