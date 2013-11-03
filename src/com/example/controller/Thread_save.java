package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Thread_save implements Runnable {
	private byte data[]=new byte[8*1024];
	static File file=null;
	static FileOutputStream fos;
	private int len;
	public Thread_save(byte[] data,int len) {
		this.data=data;
		this.len=len;
		if(file==null)
		{
			file= new File("/sdcard/SoundPlay.mp3");
			if(file.exists())
				file.delete();
			
			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public void run() {
		synchronized (this) {
			if(data!=null)
			{
				try {
					fos.write(data,0,len);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
			{
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}