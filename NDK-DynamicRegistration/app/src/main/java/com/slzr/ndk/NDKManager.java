package com.slzr.ndk;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by pxl on 2019/8/30 0030 下午 2:49.
 * Describe:
 */
public class NDKManager {

    static {
        System.loadLibrary("native-lib");
    }


    public native String stringFromJNI();
//    public native String stringFromJNI2();
    public native void initNative();
    public native int add(int a, int b);

    public void onNativeMessageReceive(final byte[] message) {
        String str=null;
        try {
            str = new String(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("NDKManager","这是来自ndkmanger的信息" + str);
    }

}
