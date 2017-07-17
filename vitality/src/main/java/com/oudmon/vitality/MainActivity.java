package com.oudmon.vitality;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean isRuning = false;
    private boolean isPermission = false;
    private VitalityView vitalityView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vitalityView = (VitalityView) findViewById(R.id.vitality);
        button = (Button) findViewById(R.id.start);
        button.setText("开始检测");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPermission) {
                    Toast.makeText(MainActivity.this, "没有语音权限！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isRuning) {
                    isRuning = false;
                    ((Button) v).setText("开始检测");
                    vitalityView.stop();
                } else {
                    isRuning = true;
                    ((Button) v).setText("停止检测");
                    vitalityView.start();
                }
            }
        });
        requestPermissions();
    }


    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启语音权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
        } else {
            //请求获取语音权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 111);
        }
    }

    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {

            Log.i(TAG, "0: " + grantResults[0] + ", 1: " + grantResults.length);

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermission = true;
            } else {
                Toast.makeText(this, "已拒绝权限！", Toast.LENGTH_SHORT).show();
                isPermission = false;
            }
        }
    }
}
