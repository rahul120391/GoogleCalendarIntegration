package com.example.googlecalenderapi;

import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class RequestTokenActivity extends Activity {

    String authorized_token;
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	super.onCreate(savedInstanceState);
    }

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println("inside on newintent");
        StringTokenizer str=new StringTokenizer(intent.getData().toString(),"=");
        while(str.hasMoreElements())
        {
        	System.out.println("token"+str.nextElement().toString());
        	authorized_token=str.nextElement().toString();
        }
        Intent i=new Intent();
        i.putExtra("token",authorized_token);
        setResult(RESULT_OK,i);
	}
}
