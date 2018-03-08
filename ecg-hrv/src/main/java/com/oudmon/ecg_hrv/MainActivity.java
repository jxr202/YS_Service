package com.oudmon.ecg_hrv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.oudmon.algo.ecgBandHRVAnalyze.EcgBandHRVAnalyzer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Jxr35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EcgBandHRVAnalyzer analyzer = EcgBandHRVAnalyzer.ecgBandHRVResultFromRRIntervals(DataUtils.arrays, 128, 25, "./fatigue.model");
        String json = analyzer.getHrvInfoJson();
        Log.i(TAG, "json: " + json);

        /*TextView textView = findViewById(R.id.textView);
        textView.setText(json);*/

        try {
            StringBuilder sb = new StringBuilder("{\n");
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                sb.append(key).append(": ").append(value).append("\n");
            }
            sb.append("}");
            TextView textView = findViewById(R.id.textView);
            textView.setText(sb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
