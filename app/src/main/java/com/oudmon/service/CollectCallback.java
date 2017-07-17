package com.oudmon.service;

import com.oudmon.algo.ppg.PpgAnalyzer;

/**
 * Created by jxr20 on 2017/6/22
 */

public interface CollectCallback {

    void onUpdate(PpgAnalyzer analyzer);

}
