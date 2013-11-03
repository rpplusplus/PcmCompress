package com.example.unit;

import android.content.Context;
import android.widget.Toast;

public class Base {
	public static Boolean isDebug=true;
	public static void sysout(String log)
	{
		if(isDebug)
			System.out.println(log);
		
	}
	public static void systouch(Context c,String log)
	{
		if(isDebug)
			Toast.makeText(c, log, 1).show();
	}
	
}
