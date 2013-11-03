package com.example.controller;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.widget.Adapter;

import com.example.unit.Base;
import com.tietie.postcard.func.LameEncoder;

public class RecordUnit {


	// 缓冲区字节大小
	private static int bufferSizeInBytes = 0;
	// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025，8000
	private static int sampleRateInHz = 44100;
	// 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private int audioSource = MediaRecorder.AudioSource.MIC;
	private static AudioRecord audioRecord;
	public static boolean isRecord = false;// 设置正在录制的状态
	Thread thread;
	LameEncoder lameEncoder;

	private void createAudioRecord() {
		lameEncoder=new LameEncoder();
		lameEncoder.onCreate();
		// 获得缓冲区字节大小
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
				channelConfig, audioFormat);
		Base.sysout("+++++++++++spakingView bufferSizeInBytes1+" + bufferSizeInBytes);
		if (bufferSizeInBytes == -2) {
			bufferSizeInBytes = 2048;
		}
		Base.sysout("++++++bufferSizeInBytes2" + bufferSizeInBytes);
		// 创建AudioRecord对象
		audioRecord = new AudioRecord(audioSource, sampleRateInHz,
				channelConfig, audioFormat, bufferSizeInBytes);
		Base.sysout("++++++SpakingView bufferSizeInBytes3=" + bufferSizeInBytes);
		Base.sysout("++++++SpakingVi9ew audioRecord stae" + audioRecord.getState());
	}
	public void startRecord() {
		createAudioRecord();
		Base.sysout("Speakingview audioRecoder state++++++" + audioRecord.getState());
		
		audioRecord.startRecording();
		// 让录制状态为true
		isRecord = true;
		// 开启音频文件写入线程
		thread = new Thread(new AudioRecordThread());
		thread.start();
	}




	public  void stopRecord() {
		if(isRecord==false)
			return;
		isRecord=false;
		new Thread_saveRaw(null,0);
		byte[] mp3Flag=new byte[16*1024];
		int len=lameEncoder.onFinish(mp3Flag);
		new Thread_save(mp3Flag,len).run();
		if (audioRecord != null) {
			Base.sysout("stopRecord");
			isRecord = false;// 停止文件写入
			audioRecord.stop();
			audioRecord.release();// 释放资源
			lameEncoder.onDestory();
		}
	}





	class AudioRecordThread extends Thread {
		@Override
		public void run() {
			int readsize = 0;
			final int size=1024*2;
			short[] audioRawbuff = new short[size];
			byte[] 	audioMp3buff = new byte[size*2];
			int len=0;
			try {
				while (isRecord) {
					readsize = audioRecord.read(audioRawbuff, 0, size);
					if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
						new Thread_saveRaw(audioRawbuff,readsize).run();
						Base.sysout("size:"+readsize);
						len=lameEncoder.onEncode(audioRawbuff, audioMp3buff);
						
						new Thread_save(audioMp3buff,len).run();

						//	                        for (int i = 1; i < 500; i += 2) {
						//	                            audiodata[size] = (byte) (audiodataShort[i]);
						//	                            if (audiodataShort[i] >=0x80) {
						//	                                audiodata[size] = (byte) ((audiodataShort[i])-0x80);
						//	                            } else {
						//	                                audiodata[size] = (byte) (0x80 + (audiodataShort[i]));
						//	                            }
						//	                            size++;
						//	                        }

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
