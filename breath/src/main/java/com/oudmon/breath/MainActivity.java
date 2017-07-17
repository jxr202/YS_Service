package com.oudmon.breath;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.oudmon.algo.breath.BreathAnalyzer;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private WaveformView wave;
    private TextView breathRate;
    private boolean isRuning = false;
    private boolean isPermission = false;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            wave.updateAmplitude((float) (msg.obj) * 0.5f / 2000);
            return false;
        }
    });
    private AudioRecoderUtils utils = AudioRecoderUtils.getInstance(mHandler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wave = (WaveformView) findViewById(R.id.wave);

        breathRate = (TextView) findViewById(R.id.breathRate);

        button = (Button) findViewById(R.id.start);
        button.setText("开始录音");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPermission) {
                    Toast.makeText(MainActivity.this, "没有语音权限！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isRuning) {
                    isRuning = false;
                    button.setText("开始录音");
                    utils.stop().convertWaveFile();
                    putBreathRate();
                } else {
                    isRuning = true;
                    button.setText("停止录音");
                    utils.start().recordData();
                }
            }
        });

        requestPermissions();
    }


    private void putBreathRate() {
        int rate = BreathAnalyzer.breathRateFromWavFile(Environment.getExternalStorageDirectory() + "/杨树大健康/breath.wav");
        breathRate.setText("呼吸频率结果: " + rate);
    }


    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启语音权限
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            isPermission = true;
        } else {
            //请求获取语音权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 111);
        }
    }

    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) ) {
                isPermission = true;
            } else {
                Toast.makeText(this, "已拒绝权限！", Toast.LENGTH_SHORT).show();
                isPermission = false;
            }
        }
    }

}
