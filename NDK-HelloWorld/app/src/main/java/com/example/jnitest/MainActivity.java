package com.example.jnitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static {        // 加载动态库
        System.loadLibrary("TestJNI");
    }

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.text_hello);
        Jni jni = new Jni();
        tv.setText(jni.say("Hello world. This message is from Jni."));

    }
}
