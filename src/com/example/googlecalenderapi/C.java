package com.example.googlecalenderapi;

public class C 
{
	   public static final String TAG = "GoogleCalender";
	  //
		//
		public static final String CONSUMER_KEY 	= "92327762356-lghj9909bt0fnv61fhnjr55jabegjeoc.apps.googleusercontent.com";
		public static final String CONSUMER_SECRET 	= "kAOXWM3spj2qaxQwOJ86z_Pr";
		public static final String SCOPE 			= "https://www.googleapis.com/auth/youtubepartner-channel-audit";
		public static final String REQUEST_URL 		= "https://accounts.google.com/o/oauth2/OAuthGetRequestToken";
		public static final String ACCESS_URL 		= "https://accounts.google.com/o/oauth2/OAuthGetAccessToken";  
		public static final String AUTHORIZE_URL 	= "https://accounts.google.com/o/oauth2/OAuthAuthorizeToken";
		public static final String GET_CONTACTS_FROM_GOOGLE_REQUEST= "https://www.google.com/m8/feeds/contacts/default/full?alt=json";
		public static final String ENCODING 		= "UTF-8";
		public static final String	OAUTH_CALLBACK_SCHEME	= "com.example.googlecalenderapi";
		public static final String	OAUTH_CALLBACK_HOST		= "oauth2Callback";
		public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + ":/" + OAUTH_CALLBACK_HOST;
		public static final String	APP_NAME                = "googlecalender";
		public static final String uri="urn:ietf:wg:oauth:2.0:oob";
		public static final String URL="https://accounts.google.com/o/oauth2/auth?client_id="+CONSUMER_KEY+"&redirect_uri="+OAUTH_CALLBACK_URL+"&scope="+"https://www.googleapis.com/auth/calendar"+"&response_type="+"code"+"&access_type="+"offline";
		public static final String Calender_Url="https://www.googleapis.com/calendar/v3/users/me/calendarList";
		public static final String Calender_event="https://www.googleapis.com/calendar/v3/calendars/primary/events";
		
}
