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

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import android.content.SharedPreferences;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.util.Log;

public class Application extends android.app.Application {
	static String TAG = "Application";
	public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
	private SerialPort mSerialPort = null;

	public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			/* Read serial port parameters */
			SharedPreferences sp = getSharedPreferences(getPackageName()+"_preferences", MODE_PRIVATE);
			String path = sp.getString("DEVICE", getString(R.string.devices_def));
			int baud_rate = Integer.decode(sp.getString("BAUDRATE", getString(R.string.baud_rate_def)));
			int data_bits = Integer.decode(sp.getString("DATA", getString(R.string.data_bits_def)));
			int stop_bits = Integer.decode(sp.getString("STOP", getString(R.string.stop_bits_def)));
			int flow = 0;
			int parity = 'N';
			String flow_ctrl = sp.getString("FLOW", getString(R.string.flow_control_def));
			String parity_check = sp.getString("PARITY", getString(R.string.parity_check_def));
			/* Check parameters */
			if ( (path.length() == 0) || (baud_rate == -1)) {
				throw new InvalidParameterException();
			}
			Log.d(TAG, "path = " + path);
			if(flow_ctrl.equals("RTS/CTS"))
				flow = 1;
			else if(flow_ctrl.equals("XON/XOFF"))
				flow = 2;
			
			if(parity_check.equals("Odd"))
				parity = 'O';
			else if(parity_check.equals("Even"))
				parity = 'E';
			
			/* Open the serial port */
			mSerialPort = new SerialPort(new File(path), baud_rate,flow,data_bits,stop_bits,parity);
		}
		return mSerialPort;
	}

	public void closeSerialPort() {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
