package com.oudmon.vitality;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by jxr20 on 2017/7/18
 */

public class MediaRecorderUtils implements Runnable {

    private static final String TAG = "MediaRecorderUtils";
    private static String PATH = Environment.getExternalStorageDirectory() + "/杨树大健康/breath.amr";
    private static boolean isAlive;
    private static MediaRecorder mMediaRecorder;
    private static Callback mCallback;

    public static void prepare(Callback callback) {
        mCallback = callback;
    }

    public static void start() {
        if (isAlive) {
            return;
        }
        Log.i(TAG, "path: " + PATH);
        try {
            isAlive = true;
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setOutputFile(PATH);
            mMediaRecorder.setAudioSamplingRate(16000);
            mMediaRecorder.setMaxDuration(1000 * 60 * 10);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (isAlive) {
            isAlive = false;
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    public void run() {
        Log.i(TAG, "isAlive: " + isAlive);
        while (isAlive) {
            int maxAmplitude = mMediaRecorder.getMaxAmplitude();
            double ratio = (double) maxAmplitude / 1;
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用

            mCallback.onUpdate(db);

            Log.i(TAG, "amplitude: " + maxAmplitude + ", ratio: " + ratio + ", db: " + db);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于回调数据，实时显示趋势
     */
    interface Callback {
        /**
         * 更新数据
         * @param data 数据
         */
        void onUpdate(double data);
    }

}
