package com.oudmon.vitality;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jxr20 on 2017/6/17
 */

public class VitalityView extends View implements AudioRecoderUtils.Callback {

    private static final String TAG = "MainActivity";
    private boolean isAlive = false;
    private Paint mPaint;
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

    /*@Override
    protected void onAttachedToWindow() {
        Log.i(TAG, "VitalityView.. onAttachedToWindow..");
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "VitalityView.. onDetachedFromWindow..");
        super.onDetachedFromWindow();
        stop();
    }*/

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
        if (!isAlive) {
            isAlive = true;
            AudioRecoderUtils.prepare(this);
            AudioRecoderUtils.start();
            AudioRecoderUtils.updateCallback();
        }
    }

    public void stop() {
        if (isAlive) {
            isAlive = false;
            AudioRecoderUtils.stop();
            AudioRecoderUtils.saveWavFile();
        }
    }

    public void setVolume(int volume) {
        if (volume < 50) {
            volume = 0;
        }
        mPointArray.add(new PointF(-mSpeedX, getHeight() / 2 - volume * 2));
        postInvalidate();
    }



    @Override
    public void onUpdate(double data) {
        Log.i(TAG, "onUpdate.. data: " + data);
        setVolume((int) data);
    }
}
