package com.example.pcmcompress;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.controller.RecordUnit;
import com.example.unit.Base;
import com.tietie.postcard.func.*;
public class MainActivity extends Activity {
	
	Button btn_startrecord;
	Button btn_stoprecord;
	RecordUnit recordUnit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_startrecord=(Button)findViewById(R.id.btn_startrecord);
		btn_stoprecord=(Button)findViewById(R.id.btn_stoprecord);
		btn_startrecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recordUnit=new RecordUnit();
				recordUnit.startRecord();
				
			}
		});
		btn_stoprecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				recordUnit.stopRecord();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	
	
	

}
