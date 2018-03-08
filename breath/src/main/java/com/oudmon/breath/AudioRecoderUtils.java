package com.oudmon.breath;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by jxr20 on 2017/6/16
 */

class AudioRecoderUtils {

    private static final String PATH = Environment.getExternalStorageDirectory() + "/杨树大健康/";

    private static final int SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int RATE = 44100;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(RATE, CHANNEL, FORMAT);

    private static AudioRecord mRecorder;

    //记录播放状态
    private static boolean isRecording = false;
    //数字信号数组
    private static byte [] noteArray;
    //PCM文件
    private static File pcmFile;
    //WAV文件
    private static File wavFile;
    //文件输出流
    private static OutputStream os;

    //wav文件目录
    private static String outFileName = PATH + "/breath.wav";
    //pcm文件目录
    private static String inFileName = PATH + "/breath.pcm";

    private static Calendar calendar = Calendar.getInstance();

    private static Handler mHandler;


    /**
     * 初始化数据
     * @param handler 回调数据
     */
    static void prepare(Handler handler) {
        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        pcmFile = new File(inFileName);
        wavFile = new File(outFileName);
        if (pcmFile.exists()) {
            pcmFile.delete();
        }
        if (wavFile.exists()) {
            wavFile.delete();
        }
        try {
            pcmFile.createNewFile();
            wavFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder = new AudioRecord(SOURCE, RATE, CHANNEL, FORMAT, BUFFER_SIZE);
        mHandler = handler;
    }

    static void start() {
        isRecording = true;
        mRecorder.startRecording();
    }

    /**
     * 记录数据
     */
    static void recordData(){
        new Thread() {
            public void run() {
                write();
            }
        }.start();
    }

    private static void write() {
        noteArray = new byte[BUFFER_SIZE];
        try {
            os = new BufferedOutputStream(new FileOutputStream(pcmFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (isRecording) {
            int recordSize = mRecorder.read(noteArray, 0, BUFFER_SIZE);
            if (recordSize > 0) {
                try {
                    int voice = 0;
                    for (byte b : noteArray) {
                        voice += b * b;
                    }
                    mHandler.sendMessage(mHandler.obtainMessage(111, voice * 1f / recordSize));
                    os.write(noteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void stop() {
        isRecording = false;
        mRecorder.stop();
    }

    static void convertWaveFile() {
        FileInputStream in;
        FileOutputStream out;
        long totalAudioLen;
        long totalDataLen;
        int channels = 1;
        long byteRate = 16 * RATE * channels / 8;
        byte[] data = new byte[BUFFER_SIZE];
        try {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(outFileName);
            totalAudioLen = in.getChannel().size();
            //由于不包括RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, RATE, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，
     * wave是RIFF文件结构，
     * 每一部分为一个chunk，
     * 其中有RIFF WAVE chunk， FMT Chunk，Fact chunk,Data chunk,
     * 其中Fact chunk是可以选择的，
     */
    private static void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                            int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

}
