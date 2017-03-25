package com.example.googlecalenderapi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends Activity {

	public static final String API_KEY = "AIzaSyAoeVE4V6fQ10FymO5enVcAGWod0qBol_g";
	Button btn_submit;
	String authorized_token;
	String access_token, refresh_token;
	String username;
	int responseCode;
	public static final String URL = "https://accounts.google.com/o/oauth2/token";
	ProgressDialog dialog;
	URL url;
	String response;
	Button btn_send;
	JSONObject jobject;
	SimpleDateFormat sd;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
	    sd=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
		System.out.println(sd.format(new Date()));
		System.out.println(toRFC3339(new Date()));
		btn_send=(Button)findViewById(R.id.btn_setevent);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(C.URL));
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_FROM_BACKGROUND);
				startActivity(intent);
			}
		});
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(access_token!=null)
				{
					new setevents().execute("");
				}
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("inside on newintent");
		if(intent.getData()!=null)
		{
			StringTokenizer str = new StringTokenizer(intent.getData().toString(),
					"=");
			while (str.hasMoreElements()) {
				System.out.println("token" + str.nextElement().toString());
				authorized_token = str.nextElement().toString();
			}
			System.out.println("authorized token" + authorized_token);
		}
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("inside on resume");
		if (authorized_token != null) {
			System.out.println("inside this");
			new fetch().execute(URL);
		}
	}

	class fetch extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(
						"https://accounts.google.com/o/oauth2/token");
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("code", authorized_token));
				pairs.add(new BasicNameValuePair("client_id", C.CONSUMER_KEY));
				pairs.add(new BasicNameValuePair("redirect_uri",
						C.OAUTH_CALLBACK_URL));
				pairs.add(new BasicNameValuePair("grant_type",
						"authorization_code"));
				post.setEntity(new UrlEncodedFormEntity(pairs));
				org.apache.http.HttpResponse response = client.execute(post);
				String responseBody = EntityUtils
						.toString(response.getEntity());
				jobject = new JSONObject(responseBody);
				Log.v("message", responseBody);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (jobject != null) {
				access_token = jobject.optString("access_token");
				refresh_token = jobject.optString("refresh_token");
			}
			new rahul().execute("");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (android.os.Build.VERSION.SDK_INT > 11) {
				dialog = new ProgressDialog(MainActivity.this,
						AlertDialog.THEME_HOLO_LIGHT);
			} else {
				dialog = new ProgressDialog(MainActivity.this);
			}
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
		}

	}

	public class rahul extends AsyncTask<String, Void, Void> {
		HttpGet httpGet;
		StringBuilder builder;
		JSONObject jObj;
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				System.out.println(params[0]);
				jObj = CommonClass.getJSON(C.Calender_event+"?access_token="+access_token);
				System.out.println("jsonobject" + jObj);
			}
			catch (Exception e)
			{
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
			}
			if(jobject!=null)
			{
				username=jObj.optString("summary");
			}
	
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

	class refreshtoken extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(
						"https://accounts.google.com/o/oauth2/token");
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("client_id", C.CONSUMER_KEY));
				pairs.add(new BasicNameValuePair("refresh_token", refresh_token));
				pairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
				post.setEntity(new UrlEncodedFormEntity(pairs));
				org.apache.http.HttpResponse response = client.execute(post);
				String responseBody = EntityUtils
						.toString(response.getEntity());
				jobject = new JSONObject(responseBody);
				Log.v("message", responseBody); // That just logs it into logCat
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
			}
			if (jobject != null) {
				access_token = jobject.optString("access_token");
			}
			new rahul().execute("");
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}
	class setevents extends AsyncTask<String,String, String>
	{
        StringBuilder builder;
		@SuppressWarnings("deprecation")
		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... params)
		{
			String summary="pathankot";
			String description="i have to go for lunch";
			String location="ludhiana";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    	final long hoursInMillis = 48L*60L * 60L * 1000L;
	    	@SuppressWarnings("deprecation")
	    	Date d=new Date();
	    	String startdate=dateFormat.format(d);
	    
			Date newDate = new Date(new Date().getTime()+(2L * hoursInMillis));
			String enddate=dateFormat.format(newDate);
			try {
				String url = "https://www.googleapis.com/calendar/v3/calendars/"+ "primary" + "/events?access_token="+access_token;
				JSONObject jsonobj=new JSONObject();
				jsonobj.put("description",description);
				jsonobj.put("location",location);
				jsonobj.put("summary",summary);
				JSONObject jsonstartdate=new JSONObject();
				jsonstartdate.put("date",startdate);
				JSONObject jsonenddate=new JSONObject();
				jsonenddate.put("date",enddate);
				jsonobj.put("end", jsonenddate);
				jsonobj.put("start", jsonstartdate);
				System.out.println("josn value"+jsonobj);
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				post.addHeader("Content-Type", "application/json");
				post.addHeader("x-li-format", "json");
				builder=new StringBuilder();
				post.setEntity(new StringEntity(jsonobj.toString()));
				org.apache.http.HttpResponse res = httpclient.execute(post);
				InputStream is = res.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				System.out.println(builder.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if(dialog!=null)
			{
				dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			if(dialog==null || !dialog.isShowing())
			{
				if (android.os.Build.VERSION.SDK_INT > 11) {
					dialog = new ProgressDialog(MainActivity.this,
							AlertDialog.THEME_HOLO_LIGHT);
				} else {
					dialog = new ProgressDialog(MainActivity.this);
				}
				dialog.setMessage("Sending...");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}
			
		}
		
	}
	public String  toRFC3339(Date d)
	{
		return sd.format(d).replace("(\\d\\d)(\\d\\d)$", "$1:$2");
	}
	
}
