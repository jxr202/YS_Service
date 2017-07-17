package com.oudmon.service;

/**
 * Created by jxr20 on 2017/6/22
 */

public class JNI_Utils {

    static {
        //System.loadLibrary("MyLibrary");
        System.loadLibrary("native-lib");   // Used to load the 'native-lib' library on application startup.
    }

    public native String getValue();

}
