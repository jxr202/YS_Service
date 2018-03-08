package com.jxr202.msmlistener;

import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 注意，短信监听在某些机型需要重启后生效，比如vivo
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "jxr";
    private SMSObserver mObserver = new SMSObserver(this, new Handler());
    private SMSReceiver mReceiver = new SMSReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObserver();
        initReceiver();
        getKeyHash();
        getSignature();
    }

    private void getKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.oudmon.bandvt", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void getSignature() {
        PackageManager manager = getPackageManager();
        StringBuilder builder = new StringBuilder();
        //String pkgname = getPackageName();
        String pkgname = "com.oudmon.bandvt";
        boolean isEmpty = pkgname.isEmpty();
        if (isEmpty) {
            Toast.makeText(this, "应用程序的包名不能为空！", Toast.LENGTH_SHORT);
        } else {
            try {

                PackageInfo packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
                Signature[] signatures = packageInfo.signatures;
                Signature sign = signatures[0];

                byte[] signByte = sign.toByteArray();
                Log.e(TAG, "getSingInfo: " + bytesToHexString(signByte));
                Log.e(TAG, "getSingInfo hash: " + bytesToHexString(generateSHA1(signByte)));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] generateSHA1(byte[] data) {
        try {
            // 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // 开始使用算法
            messageDigest.update(data);
            // 输出算法运算结果
            byte[] hashValue = messageDigest.digest(); // 20位字节
            return hashValue;
        } catch (Exception e) {
            Log.e("generateSHA1", e.getMessage());
        }
        return null;

    }

    public String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        for (byte aByte : bytes) {
            if ((aByte & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(aByte & 0xff));
        }
        return buff.toString();
    }


    private void initObserver() {
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
