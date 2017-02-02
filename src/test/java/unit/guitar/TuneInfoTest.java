package unit.guitar;


import org.junit.Assert;
import org.junit.Test;
import pitchdetection.guitar.GuitarNote;
import pitchdetection.guitar.TuneInfo;

public class TuneInfoTest {
    @Test
    public void testStringRepresentation() {
        TuneInfo tuneInfo = new TuneInfo(GuitarNote.E2, 23);
        Assert.assertEquals(tuneInfo.toString(), "E2 +23,00");
        tuneInfo = new TuneInfo(GuitarNote.E2, 0.3456f);
        Assert.assertEquals(tuneInfo.toString(), "E2 +0,35");
        tuneInfo = new TuneInfo(GuitarNote.E2, -10);
        Assert.assertEquals(tuneInfo.toString(), "E2 -10,00");
    }
}
