package com.tietie.postcard.func;

public class LameEncoder
{
	static
	{
		System.loadLibrary("mp3lame");
	}
	public native void onCreate();
	public native int onEncode(short[] inputBuffer, byte[] outputBuffer);
	public native int onFinish(byte[] outputBuffer);
	public native void onDestory();
	
}