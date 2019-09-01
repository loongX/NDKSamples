package com.example.ndktest2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.slzr.ndk.NDKManager;

public class MainActivity extends AppCompatActivity {

    NDKManager mNdkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        mNdkManager = new NDKManager();
        mNdkManager.initNative();
        tv.setText(mNdkManager.stringFromJNI());
//        mNdkManager.stringFromJNI2();
        mNdkManager.add(1,2);



    }


}
