package com.zistone.gpio;

public class Gpio {

	private static Gpio mGpioSet = new Gpio();
	
	private Gpio(){ }

	public static Gpio getInstance(){
		return mGpioSet; 
	}
	public native void set_gpio(int state,int pin_num);
	static {
		System.loadLibrary("gpio");
	}
	
}
