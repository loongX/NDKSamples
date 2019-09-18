/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.example.pasmcardtest;

import java.io.IOException;
import java.util.Locale;
import com.zistone.gpio.Gpio;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.serialport.SerialPortManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
	private static String TAG = "MainActivity";
	private EditText mReception;
	private EditText mTransmit;
	private Button btn_poweron;
	private Button btn_random;
	private Button btn_poweroff;
	private Button btn_apdu;
	private Button btn_exit;
	private int send_mode = 2;
	private MySerialPortManager mMySerialPortManager;
	
	private final static byte[] mPowerOnBuf = new byte[]{(byte)0x62, (byte)0x00, (byte)0x00, (byte)0x00, 
		(byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x60};
	private final static byte[] mRandomBuf = new byte[]{0x62, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x60};
	private final static byte[] mPowerOffBuf = new byte[]{0x63, 0x00, 0x00, 0x00, 0x00, 0x00, 0x13, 0x00, 0x00, 0x00, 0x70};
	
	private final static char[] mChars = "0123456789ABCDEF".toCharArray();  
    private final static String mHexStr = "0123456789ABCDEF";
    
    //隐藏软键盘
	protected void hideSoftInput(View view)
	{
		view.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMySerialPortManager = new MySerialPortManager(this);
		Gpio.getInstance().set_gpio(81, 1);
		mReception = (EditText) findViewById(R.id.EditTextReception);
		hideSoftInput(mReception);
		
		mTransmit = (EditText) findViewById(R.id.EditTextTransmit);
	    
		btn_poweron = (Button) findViewById(R.id.poweron);
		btn_poweron.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				mMySerialPortManager.onDataReceived(mPowerOnBuf, mPowerOnBuf.length);
				mMySerialPortManager.write(mPowerOnBuf);
			}
		});
		
		btn_apdu = (Button) findViewById(R.id.apdu);
		btn_apdu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String send = mTransmit.getText().toString();
				Log.d(TAG, "send = " + send);
				mMySerialPortManager.write(hexStr2Str(send));
			}
		});
		
		btn_exit = (Button) findViewById(R.id.exit);
		btn_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume");
		int retOpen = mMySerialPortManager.openSerialPort();
		Log.d(TAG, "retOpen = "+retOpen);
		if(retOpen == 0 || retOpen == 1){
			btn_poweron.setEnabled(true);
			btn_apdu.setEnabled(true);
		}else{
			btn_poweron.setEnabled(false);
			btn_apdu.setEnabled(false);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult");
		Log.d(TAG, "resultCode = "+resultCode);
		if(resultCode == 1){
			mMySerialPortManager.closeSerialPort();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMySerialPortManager.closeSerialPort();
		Gpio.getInstance().set_gpio(81, 0);
	}

	class MySerialPortManager extends SerialPortManager{
		
		public MySerialPortManager(Context context) {
			// TODO Auto-generated constructor stub
			super(context);
		}

		protected void onDataReceived(final byte[] buffer, final int size) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (mReception != null) {
						Log.d(TAG, "recv = " + byte2hex(buffer, size));
						mReception.append(byte2hex(buffer, size));
					}
					mReception.setSelection(mReception.length());
				}
			});
		}
		public void write(byte[] data){
			try {
				mOutputStream.write(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static byte[] hexStr2Str(String hexStr){    
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);  
        char[] hexs = hexStr.toCharArray();    
        byte[] bytes = new byte[hexStr.length() / 2];    
        int iTmp = 0x00;
        for (int i = 0; i < bytes.length; i++){    
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;    
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);    
            bytes[i] = (byte) (iTmp & 0xFF);    
        }    
        return bytes;    
    } 
	
    public static final String byte2hex(byte b[], int size) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < size; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	// TODO Auto-generated method stub
    	Log.d(TAG, "featureId = "+featureId);
    	Log.d(TAG, "item = "+item);
    	if(featureId == 0){
    		Intent intent = new Intent(MainActivity.this , SerialPortActivity.class);
    		startActivityForResult(intent, 0);
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
}
