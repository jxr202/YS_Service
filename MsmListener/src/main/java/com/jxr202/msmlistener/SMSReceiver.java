package com.jxr202.msmlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "jxr202";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "SMSReceiver ====> onReceive, 收到短信广播");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                SmsMessage msg = SmsMessage.createFromPdu((byte[]) object);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String time = sdf.format(new Date(msg.getTimestampMillis()));
                Log.i(TAG, "SMSReceiver ====> time: " + time + ", number: " + msg.getOriginatingAddress() + ", body: " + msg.getDisplayMessageBody());
            }
        }
    }
}
