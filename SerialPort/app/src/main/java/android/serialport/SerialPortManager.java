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

package android.serialport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import com.example.pasmcardtest.Application;
import com.example.pasmcardtest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.Log;

import static com.example.pasmcardtest.MainActivity.byte2hex;

public abstract class SerialPortManager{

	protected Context mContext;
	protected Application mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null){
						continue;
					}
					size = mInputStream.read(buffer);
//					Log.i(this.getName(),"run 循环中");
					if (size > 0) {
						long pid = android.os.Process.myTid();
						long tid = Thread.currentThread().getId();
						Log.i("SPTres","recMsg len : " + size +
								" pid: " + pid +
								" threadid:" + tid  +
								" mes : " + byte2hex(buffer, size)
								+ "  "+new String(buffer, 0, size) );
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(mContext);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				closeSerialPort();
			}
		});
		b.show();
	}

	public SerialPortManager(Context context) {
		mContext = context;
		mApplication = (Application) ((Activity) mContext).getApplication();
		
	}

	protected abstract void onDataReceived(final byte[] buffer, final int size);
	
	public int openSerialPort(){
		try {
			if(mSerialPort == null){
				mSerialPort = mApplication.getSerialPort();
				if(mSerialPort != null){
					mOutputStream = mSerialPort.getOutputStream();
					mInputStream = mSerialPort.getInputStream();
					mReadThread = new ReadThread();
					mReadThread.start();
					return 0;
				}
			}
			else return 1;
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
		return -1;
	}
	
	public void closeSerialPort() {
		if (mReadThread != null)
			mReadThread.interrupt();
		if(mApplication != null)
			mApplication.closeSerialPort();
		mSerialPort = null;
	}
}
