package pitchdetection.sound;

import pitchdetection.sound.interfaces.AudioInputInterface;

import java.io.DataInputStream;
import java.io.FileInputStream;

public class WavFileAudioInput implements AudioInputInterface {

    /**
     * the input stream we read from *
     */
    private final DataInputStream stream;

    /**
     * number of channels *
     */
    private final int channels;

    /**
     * sample rate in Herz*
     */
    private final float sampleRate;

    /** **/

    public WavFileAudioInput(String filename) throws Exception {
        this.stream = new DataInputStream(new FileInputStream(filename));

        if (!read4ByteString(stream).equals("RIFF")) {
            throw new IllegalArgumentException("not a wav");
        }

        readIntLittleEndian(stream);

        if (!read4ByteString(stream).equals("WAVE"))
            throw new IllegalArgumentException("expected WAVE tag");

        if (!read4ByteString(stream).equals("fmt "))
            throw new IllegalArgumentException("expected fmt tag");

        if (readIntLittleEndian(stream) != 16)
            throw new IllegalArgumentException("expected wave chunk size to be 16");

        if (readShortLittleEndian(stream) != 1)
            throw new IllegalArgumentException("expected format to be 1");

        channels = readShortLittleEndian(stream);
        sampleRate = readIntLittleEndian(stream);
        if (sampleRate != 44100) {
            throw new IllegalArgumentException("Not 44100 sampling rate");
        }
        readIntLittleEndian(stream);
        readShortLittleEndian(stream);
        int fmt = readShortLittleEndian(stream);

        if (fmt != 16)
            throw new IllegalArgumentException("Only 16-bit signed format supported");

        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        read4ByteString(stream);
        readShortLittleEndian(stream);

        if (!read4ByteString(stream).equals("data"))
            throw new RuntimeException("expected data tag");

        readIntLittleEndian(stream);
    }

    public String read4ByteString(DataInputStream stream) throws Exception {
        byte[] bytes = new byte[4];
        stream.readFully(bytes);
        return new String(bytes, "US-ASCII");
    }

    public int readIntLittleEndian(DataInputStream stream) throws Exception {
        int result = stream.readUnsignedByte();
        result |= stream.readUnsignedByte() << 8;
        result |= stream.readUnsignedByte() << 16;
        result |= stream.readUnsignedByte() << 24;
        return result;
    }

    public short readShortLittleEndian(DataInputStream stream) throws Exception {
        int result = stream.readUnsignedByte();
        result |= stream.readUnsignedByte() << 8;
        return (short) result;
    }

    @Override
    public int getInputBitRate() {
        return 44100;
    }

    @Override
    public void startRecording() {

    }

    @Override
    public byte[] readData(int bytesCount) {
        byte[] result = new byte[bytesCount];

        for (int i = 0; i < bytesCount; i++) {
            float sample = 0;
            try {
                for (int j = 0; j < channels; j++) {
                    int shortValue = readShortLittleEndian(stream);
                    sample += ((float) shortValue / (float) Short.MAX_VALUE * (float) Byte.MAX_VALUE);
                }
                sample /= channels;
                result[i] = (byte) sample;

            } catch (Exception ex) {
                break;
            }
        }

        return result;
    }
}
