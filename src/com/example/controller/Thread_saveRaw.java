package com.example.controller;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.unit.Base;

public class Thread_saveRaw implements Runnable {
	private short data[]=new short[8*1024];
	static File file=null;
	static DataOutputStream fos;
	private int len;
	public Thread_saveRaw(short[] data,int len) {
		this.data=data;
		this.len=len;
		if(file==null)
		{
			file= new File("/sdcard/SoundPlay.Pcm");
			if(file.exists())
				file.delete();
			
			try {
				file.createNewFile();
				fos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file),8*1024));
				
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
					for(int i=0;i<len;i++)
					{
						fos.writeShort(data[i]);
					}
					fos.flush();
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