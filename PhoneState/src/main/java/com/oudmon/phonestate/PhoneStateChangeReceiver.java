package com.oudmon.phonestate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "jxr";
    private boolean isOutGoingCall = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "---> onReceive: action: " + intent.getAction());
        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction())) {// 去电
            Log.i(TAG, "拨打电话=" + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
            isOutGoingCall = true;
        } else {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.i(TAG, "phone  " + state + "  number=" + intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
            String curPhone = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                //if (!isOutGoingCall) MsgPushService.pushPhoneMsg(context,1,curPhone);
            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                //if (!isOutGoingCall) MsgPushService.pushPhoneMsg(context,2,curPhone);
            } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                if (!isOutGoingCall) {
                   // MsgPushService.pushPhoneMsg(context,0,curPhone);
                }
                isOutGoingCall = false;
            }
        }
    }
}

