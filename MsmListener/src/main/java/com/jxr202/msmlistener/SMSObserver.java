package com.jxr202.msmlistener;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jxr34 on 2018/1/24
 */

public class SMSObserver extends ContentObserver {

    private static final String TAG = "jxr202";
    private static Uri SMS = Uri.parse("content://sms/inbox");
    private Handler mHandler;
    private Context mContext;


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SMSObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(TAG, "SMSObserver ====> onChange.. selfChange: " + selfChange);

        ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[]{"body", "address", "date"};
        //String where = "  date >  " + getCodeTime;
        Cursor cursor = cr.query(SMS, projection, null, null, "date desc");
        if (cursor != null) {
            boolean next = cursor.moveToNext();
            Log.i(TAG, "SMSObserver ====> next: " + next);
            if (next) {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                String phone = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                String time = sdf.format(new Date(Long.valueOf(date)));
                Log.i(TAG, "SMSObserver ====> time: " + time + ", phone: " + phone + ", body: " + body);
            }
            cursor.close();
        }

    }



}
