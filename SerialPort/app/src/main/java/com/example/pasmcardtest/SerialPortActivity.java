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

import com.example.pasmcardtest.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.serialport.SerialPortFinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

public class SerialPortActivity extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;
	private boolean isChange = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;
		addPreferencesFromResource(R.xml.serial_port_preferences);
		/*
		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});
		*/
		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
		final String device_bak = devices.getValue();
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Log.d("TAG", "newValue = " + newValue);
				preference.setSummary((String)newValue);
				if(!newValue.equals(device_bak)){
					isChange = true;
				}
				return true;
			}
		});
		
		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
		final String baudrates_bak = baudrates.getValue();
		baudrates.setSummary(baudrates.getValue());
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				if(!newValue.equals(baudrates_bak)){
					isChange = true;
				}
				return true;
			}
		});
		
		// data
		final ListPreference data = (ListPreference)findPreference("DATA");
		final String data_bak = data.getValue();
		data.setSummary(data.getValue());
		data.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				if(!newValue.equals(data_bak)){
					isChange = true;
				}
				return true;
			}
		});
		// stop
		final ListPreference stop = (ListPreference)findPreference("STOP");
		final String stop_bak = stop.getValue();
		stop.setSummary(stop.getValue());
		stop.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				if(!newValue.equals(stop_bak)){
					isChange = true;
				}
				return true;
			}
		});

		//parity
		final ListPreference parity = (ListPreference)findPreference("PARITY");
		final String parity_bak = parity.getValue();
		parity.setSummary(parity.getValue());
		parity.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				if(!newValue.equals(parity_bak)){
					isChange = true;
				}
				return true;
			}
		});
		
		//flow
		final ListPreference flow = (ListPreference)findPreference("FLOW");
		final String flow_bak = flow.getValue();
		flow.setSummary(flow.getValue());
		flow.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				if(!newValue.equals(flow_bak)){
					isChange = true;
				}
				return true;
			}
		});
	}
	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {  
	        if(isChange){
	        	setResult(1);
	        }else{
	        	setResult(0);
	        }
	        SerialPortActivity.this.finish();
	    }  
	    return super.onKeyDown(keyCode, event);  
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home: 
	    		if(isChange){
		        	setResult(1);
		        }else{
		        	setResult(0);
		        }
		        SerialPortActivity.this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
