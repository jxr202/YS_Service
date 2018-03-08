package com.jxr202.ecgwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.oudmon.algo.ecg.SuddenDeathSign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "jxr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String arrayText = getTextArray();
        Log.i(TAG, "arrayText: " + arrayText);

        String[] arrays = arrayText.split(",");
        int[] array = new int[arrays.length];
        for (int i = 0; i < arrays.length; i ++)  {
            array[i] = Integer.parseInt(arrays[i].trim());
        }

        initEcgWave(array);
    }

    private String getTextArray() {
        InputStream inputStream = getResources().openRawResource(R.raw.arrays);
        InputStreamReader reader = null;

        reader = new InputStreamReader(inputStream);

        BufferedReader bufferedReader = new BufferedReader(reader);


        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    private void initEcgWave(int[] arrays) {
        SuddenDeathSign sign = SuddenDeathSign.extractSuddenDeathSignFrom(arrays, 250);
        int[] waves = sign.getRWaveTags();
        StringBuilder sb = new StringBuilder();
        for (int wave : waves) {
            sb.append(wave).append(", ");
        }
        Log.i(TAG, "length: " + waves.length);
        Log.i(TAG, "waves: " + sb.toString());
    }



}
