package com.oudmon.service;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.oudmon.algo.ppg.PpgAnalyzerCallback;
import com.oudmon.algo.ppg.PpgAnalyzerData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends Activity implements PpgAnalyzerCallback {

    private static final String TAG = "MainActivity";
    private PhotoCollectView mSurfaceView;
    private TextView start;
    private TextView play;
    private TextView bp, hr, bo, br;
    private boolean isStart = false;
    private JNI_Utils utils = new JNI_Utils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        EventBus.getDefault().register(this);
        getPath();
    }

    private void getPath() {
        String a = getApplicationContext().getCacheDir().getAbsolutePath();
        String b = getApplicationContext().getPackageResourcePath();
        Log.i(TAG, "a: " + a + ", b: " + b);
    }


    private void initView() {
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView.setmPpgAnalyzerCallback(this);
        bp = findViewById(R.id.bp);
        hr = findViewById(R.id.hr);
        bo = findViewById(R.id.bo);
        br = findViewById(R.id.br);
        start = findViewById(R.id.start);
        play = findViewById(R.id.playing);
        start.setText(utils.getValue());
    }

    public void start(View view) {
        Log.i(TAG, "start.. isStart: " + isStart);
        isStart = !isStart;
        EventBus.getDefault().post(new Click(isStart));
        startProp(isStart);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClickCallback event) {
        isStart = event.isPlaying;
        startProp(event.isPlaying);
        Log.i(TAG, "onEvent.. isStart: " + isStart);
    }

    private void startProp(boolean isStart) {
        if (isStart) {
            start.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        } else {
            start.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onUpdate(PpgAnalyzerData data) {
        bp.setText(data.getDbp() + "/" + data.getSbp() + "mmHg");
        hr.setText(data.getHeartRate() + "次/分钟");
        bo.setText(data.getBloodOxygen() + "%");
        br.setText(data.getBreathRate() + "");
    }

    /*int mPreviewWidth, mPreviewHeight;
    boolean bIfPreview = false;

    *//*【2】【相机预览】*//*
    private void initCamera()//surfaceChanged中调用
    {
        Log.i(TAG, "going into initCamera");

        mCamera = Camera.open();// 开启摄像头（2.3版本后支持多摄像头,需传入参数）
        try
        {
            Log.i(TAG, "SurfaceHolder.Callback：surface Created");
            mCamera.setPreviewDisplay(mSurfaceHolder);//set the surface to be used for live preview

        } catch (Exception ex)
        {
            if(null != mCamera)
            {
                mCamera.release();
                mCamera = null;
            }
            Log.i(TAG+"initCamera", ex.getMessage());
        }

        Log.i(TAG, "mCamera: " +mCamera + ", bIfPreview: " + bIfPreview);

        if (bIfPreview)
        {
            mCamera.stopPreview();//stopCamera();
        }
        if(null != mCamera)
        {
            try
            {
    *//* Camera Service settings*//*
                Camera.Parameters parameters = mCamera.getParameters();
                // parameters.setFlashMode("off"); // 无闪光灯
                parameters.setPictureFormat(PixelFormat.JPEG); //Sets the image format for picture 设定相片格式为JPEG，默认为NV21
                parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP); //Sets the image format for preview picture，默认为NV21
    *//*【ImageFormat】JPEG/NV16(YCrCb format，used for Video)/NV21(YCrCb format，used for Image)/RGB_565/YUY2/YU12*//*

                // 【调试】获取caera支持的PictrueSize，看看能否设置？？
                List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
                List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
                List<Integer> previewFormats = mCamera.getParameters().getSupportedPreviewFormats();
                List<Integer> previewFrameRates = mCamera.getParameters().getSupportedPreviewFrameRates();
                Log.i(TAG+"initCamera", "cyy support parameters is ");
                Camera.Size psize = null;
                for (int i = 0; i < pictureSizes.size(); i++)
                {
                    psize = pictureSizes.get(i);
                    Log.i(TAG+"initCamera", "PictrueSize,width: " + psize.width + " height" + psize.height);
                }
                for (int i = 0; i < previewSizes.size(); i++)
                {
                    psize = previewSizes.get(i);
                    Log.i(TAG+"initCamera", "PreviewSize,width: " + psize.width + " height" + psize.height);
                }
                Integer pf = null;
                for (int i = 0; i < previewFormats.size(); i++)
                {
                    pf = previewFormats.get(i);
                    Log.i(TAG+"initCamera", "previewformates:" + pf);
                }

                // 设置拍照和预览图片大小
                parameters.setPictureSize(640, 480); //指定拍照图片的大小
                parameters.setPreviewSize(352, 288); // 指定preview的大小
                //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错

                // 横竖屏镜头自动调整
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
                {
                    parameters.set("orientation", "portrait"); //
                    parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
                    mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
                } else// 如果是横屏
                {
                    parameters.set("orientation", "landscape"); //
                    mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
                }

                *//* 视频流编码处理 *//*
                //添加对视频流处理函数


                // 设定配置参数并开启预览
                mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
                mCamera.startPreview(); // 打开预览画面
                mCamera.setPreviewCallback(this);
                bIfPreview = true;

                // 【调试】设置后的图片大小和预览大小以及帧率
                Camera.Size csize = mCamera.getParameters().getPreviewSize();
                mPreviewHeight = csize.height; //
                mPreviewWidth = csize.width;
                Log.i(TAG+"initCamera", "after setting, previewSize:width: " + csize.width + " height: " + csize.height);
                csize = mCamera.getParameters().getPictureSize();
                Log.i(TAG+"initCamera", "after setting, pictruesize:width: " + csize.width + " height: " + csize.height);
                Log.i(TAG+"initCamera", "after setting, previewformate is " + mCamera.getParameters().getPreviewFormat());
                Log.i(TAG+"initCamera", "after setting, previewframetate is " + mCamera.getParameters().getPreviewFrameRate());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }*/


}
