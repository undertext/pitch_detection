package unit.guitar;


import org.junit.Assert;
import org.junit.Test;
import pitchdetection.guitar.DefaultGuitarTuner;
import pitchdetection.guitar.GuitarNote;
import pitchdetection.guitar.TuneInfo;
import pitchdetection.guitar.interfaces.GuitarTuner;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

import static org.mockito.Mockito.*;

public class DefaultGuitarTunerTest {

    @Test
    public void testTune() {
        FundamentalFrequencyDetector frequencyDetector = mock(FundamentalFrequencyDetector.class);
        when(frequencyDetector.getFundamentalFrequency()).thenReturn(400f).thenReturn(206f).thenReturn(80f).thenReturn(0f);
        GuitarTuner tuner = new DefaultGuitarTuner(frequencyDetector);
        TuneInfo tuneInfo = tuner.tune();
        verify(frequencyDetector).getFundamentalFrequency();
        Assert.assertEquals(tuneInfo.note, GuitarNote.E4);
        Assert.assertEquals(tuneInfo.delta, 400f - GuitarNote.E4.getFrequency(), 0.01);

        tuneInfo = tuner.tune();
        Assert.assertEquals(tuneInfo.note, GuitarNote.G3);
        Assert.assertEquals(tuneInfo.delta, 206f - GuitarNote.G3.getFrequency(), 0.01);

        tuneInfo = tuner.tune();
        Assert.assertEquals(tuneInfo.note, GuitarNote.E2);
        Assert.assertEquals(tuneInfo.delta, 80f - GuitarNote.E2.getFrequency(), 0.01);

        tuneInfo = tuner.tune();
        Assert.assertEquals(tuneInfo.note, GuitarNote.E2);
        Assert.assertEquals(tuneInfo.delta, 80f - GuitarNote.E2.getFrequency(), 0.01);
    }
}
