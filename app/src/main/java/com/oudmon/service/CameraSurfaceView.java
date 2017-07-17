package com.oudmon.service;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.oudmon.algo.ppg.PpgAnalyzer;
import com.oudmon.algo.ppg.PpgAnalyzerCallback;
import com.oudmon.algo.ppg.PpgAnalyzerData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jxr20 on 2017/6/13
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "PhotoCollectView";
    private Camera mCamera = null;     // Camera对象，相机预览
    private boolean isCollect = false;


    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSurfaceView();
    }

    private void initSurfaceView() {
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        EventBus.getDefault().register(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        EventBus.getDefault().unregister(this);
        stopPreview();
        release();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Click event) {
        Log.i(TAG, "onEvent.. isStart: " + event.start);
        isCollect = event.start;
        if (event.start) {
            startPreview();
        } else {
            stopPreview();
        }
    }

    /**
     * width: 1920, height: 1080
     * width: 1440, height: 1080
     * width: 1280, height: 960
     * width: 1280, height: 720
     * width: 800, height: 480
     * width: 768, height: 432
     * width: 720, height: 480
     * width: 640, height: 480
     * width: 384, height: 288
     * width: 352, height: 288
     * width: 320, height: 240
     * width: 176, height: 144
     */
    public void startPreview() {
        Log.i(TAG, "startPreview.. ");
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(getHolder());
                Camera.Parameters params = mCamera.getParameters();
                params.setPreviewSize(352, 288);
                mCamera.setParameters(params);
                mCamera.startPreview();
                mCamera.setDisplayOrientation(90);  //旋转90度
                mCamera.setPreviewCallback(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPreview() {
        Log.i(TAG, "stopPreview.. ");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null); //！！这个必须在前，不然退出出错
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private int index = 0;
    private int dataLength = 16;
    private PpgAnalyzer mPpgAnalyzer = new PpgAnalyzer();
    private PpgAnalyzerData mPpgAnalyzerData = new PpgAnalyzerData();
    private PpgAnalyzerCallback mPpgAnalyzerCallback = null;
    private float[] imageHue = new float[dataLength];
    private float[] imageRed = new float[dataLength];
    private float[] imageBlue = new float[dataLength];

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isCollect) {
            mPpgAnalyzer = mPpgAnalyzer.ppgImageAlgo(data, 352, 288);
            imageHue[index] = mPpgAnalyzer.getHue();
            imageRed[index] = mPpgAnalyzer.getRed();
            imageBlue[index] = mPpgAnalyzer.getBlue();
            index ++;
            if (index >= dataLength) {
                index = 0;
                int heartRate = mPpgAnalyzer.ppgInstantHrAlgo(imageHue, imageHue.length);
                mPpgAnalyzerData.setHeartRate(heartRate);
                float breathRate = mPpgAnalyzer.ppgRespirAlgo(imageHue, imageHue.length);
                mPpgAnalyzerData.setBreathRate(breathRate);
                mPpgAnalyzerData.setBloodOxygen(mPpgAnalyzer.ppgSao2Algo(imageRed, imageBlue, imageRed.length));
                mPpgAnalyzer = mPpgAnalyzer.ppgBpAlgo(heartRate);
                mPpgAnalyzerData.setDbp(mPpgAnalyzer.getDbp());
                mPpgAnalyzerData.setSbp(mPpgAnalyzer.getSbp());
            }

            if (mPpgAnalyzerCallback != null) {
                mPpgAnalyzerCallback.onUpdate(mPpgAnalyzerData);
            }
            Log.i(TAG, "onPreviewFrame.. mPpgAnalyzerData: " + mPpgAnalyzerData);
        }
    }

    private void release() {
        if (mPpgAnalyzer != null) {
            mPpgAnalyzer.ppgFreeHrRes();
            mPpgAnalyzer.ppgFreeRespirRes();
        }
    }

    public void setmPpgAnalyzerCallback(PpgAnalyzerCallback mPpgAnalyzerCallback) {
        this.mPpgAnalyzerCallback = mPpgAnalyzerCallback;
    }
}
