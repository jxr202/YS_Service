package com.oudmon.vitality;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jxr20 on 2017/6/17
 */

public class VitalityView extends View implements Runnable {

    private static final String TAG = "MainActivity";
    private static final String PATH = Environment.getExternalStorageDirectory() + "/杨树大健康/vitality.amr";

    private MediaRecorder mMediaRecorder;
    private boolean isAlive = false;
    private Paint mPaint;

    private float volume = 10;
    private long mSpeedX = 0;
    private ArrayList<PointF> mPointArray;


    public VitalityView(Context context) {
        super(context);
        init();
    }

    public VitalityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0xffFE7F41);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPointArray = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPointArray.size() > getWidth() / 5) {
            mPointArray.remove(0);
        }

        canvas.save();
        canvas.translate(mSpeedX, 0);
        for (int i = mPointArray.size() - 1; i >= 0; i --) {
            PointF p = mPointArray.get(i);
            canvas.drawLine(p.x, p.y, p.x, getHeight() / 2, mPaint);
        }
        canvas.restore();
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        mSpeedX += 5;
    }

    public void start() {
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
        new Thread(this).start();
    }

    public void stop() {
        if (isAlive) {
            isAlive = false;
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void setVolume(int volume) {
        if (volume < 50) {
            volume = 0;
        }
        this.volume = volume;
        mPointArray.add(new PointF(-mSpeedX, getHeight() / 2 - volume * 2));
        postInvalidate();
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
            setVolume((int) (db));

            Log.i(TAG, "amplitude: " + maxAmplitude + ", ratio: " + ratio + ", db: " + db);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
