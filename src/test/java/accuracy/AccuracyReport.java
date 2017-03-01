package accuracy;

import freemarker.template.Configuration;
import freemarker.template.Template;
import pitchdetection.guitar.DefaultGuitarTuner;
import pitchdetection.guitar.GuitarNote;
import pitchdetection.guitar.TuneInfo;
import pitchdetection.guitar.interfaces.GuitarTuner;
import pitchdetection.sound.FFTFundamentalFrequencyDetector;
import pitchdetection.sound.FakeAudioInput;
import pitchdetection.sound.WavFileAudioInput;
import pitchdetection.sound.YINFundamentalFrequencyDetector;
import pitchdetection.sound.interfaces.AudioInputInterface;
import pitchdetection.sound.interfaces.FundamentalFrequencyDetector;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for generating accuracy report of pitch detection algorithms.
 */
public class AccuracyReport {

    public static void main(String[] args) throws Exception {

        ClassLoader classLoader = AccuracyReport.class.getClassLoader();
        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_0);

        Map<String, Object> algorithmsResult = new HashMap<String, Object>();
        freemarkerConfiguration.setClassForTemplateLoading(AccuracyReport.class, "/");

        List<Class> frequencyDetectorClasses = new ArrayList<Class>();
        // We have only FFT based pitch detection method for now.
        frequencyDetectorClasses.add(FFTFundamentalFrequencyDetector.class);

        List fDetectorResults = new ArrayList();

        for (Class frequencyDetectorClass : frequencyDetectorClasses) {
            Map<String, Object> fDetectorResult = new HashMap<String, Object>();
            Map<String, List> algorithmData = new HashMap<String, List>();
            Map<String, Double> algorithmDeviation = new HashMap<String, Double>();
            for (GuitarNote note : GuitarNote.values()) {
                double overallDeviation = 0;
                List algorithmResults = new ArrayList();
                if (classLoader.getResource("sound/" + note.name().toLowerCase() + ".wav") != null) {
                    File file = new File(classLoader.getResource("sound/" + note.name().toLowerCase() + ".wav").getFile());
                    if (file != null) {
                        AudioInputInterface audioInput = new WavFileAudioInput(file.getAbsolutePath());
                        FundamentalFrequencyDetector frequencyDetector = (FundamentalFrequencyDetector) frequencyDetectorClass.getConstructors()[0].newInstance(audioInput, 4096);
                        GuitarTuner tuner = new DefaultGuitarTuner(frequencyDetector);
                        for (int i = 0; i < 100; i++) {
                            Map<String, Object> resultForFrequency = new HashMap<String, Object>();
                            tuner.tune();
                            float fundamentalFrequency = frequencyDetector.getCurrentFundamentalFrequency();
                            if (fundamentalFrequency != 0) {
                                double freq = note.getFrequency();
                                resultForFrequency.put("note", note.name());
                                resultForFrequency.put("chunk", i);
                                resultForFrequency.put("original", freq);
                                resultForFrequency.put("detected", fundamentalFrequency);
                                resultForFrequency.put("deviation", fundamentalFrequency - freq);
                                resultForFrequency.put("deviationPercents", Math.abs(fundamentalFrequency - freq) / freq);
                                overallDeviation += Math.abs(fundamentalFrequency - freq);
                                algorithmResults.add(resultForFrequency);
                            }
                        }

                        algorithmDeviation.put(note.name(), overallDeviation / algorithmResults.size());
                    }
                }

                algorithmData.put(note.name(), algorithmResults);

            }

            double overallDeviation = 0;
            for (Double accuracy : algorithmDeviation.values()) {
                overallDeviation += accuracy/ 6.0;
            }

            fDetectorResult.put("class", frequencyDetectorClass.toString());
            fDetectorResult.put("res", algorithmData);
            fDetectorResult.put("deviations", algorithmDeviation);
            fDetectorResult.put("overallDeviation", overallDeviation);


            fDetectorResults.add(fDetectorResult);
        }
        algorithmsResult.put("algs", fDetectorResults);


        Template template = freemarkerConfiguration.getTemplate("comprasion.ftlh");
        Writer out = new FileWriter("docs/comprasion.html");
        template.process(algorithmsResult, out);

    }
}
