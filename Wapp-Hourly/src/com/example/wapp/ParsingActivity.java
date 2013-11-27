package com.example.wapp;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ParsingActivity extends ListActivity {

	// url to make request
		private static String url;
		private static String url1 = "http://api.openweathermap.org/data/2.5/history/city?q=";
		
		
		private static String url2 = "&APPID=615efdeead9cc534ff779cff61ddeb0c&type=daily&cnt=30";

	
	
	// JSON Nodes
	private static final String TAG_DATE = "dt";
	private static final String TAG_LIST = "list";
	private static final String TAG_SPEED = "speed";
	private static final String TAG_CITY_ID  = "city_id";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_WEATHER = "weather";
	private static final String TAG_MAIN = "main";
	private static final String TAG_MAIN1 = "main";
	private static final String TAG_TEMP = "temp";
	private static final String TAG_TEMP_MIN = "temp_min";
	private static final String TAG_TEMP_MAX = "temp_max";
	private static final String TAG_PRESSURE = "pressure";
	private static final String TAG_HUMIDITY = "humidity";
	private static final String TAG_WIND = "wind";
	
	
	JSONArray list = null;
	
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent intent = getIntent();
		String city = intent.getStringExtra(MainActivity.CITY_SYMBOL);
		setTitle(city.toUpperCase() + " -Tap Date To Get Details");
		Log.i("CITY", city);
		url = url1 + city + url2;
		
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> dateList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance
		Parser jParser = new Parser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		Log.i("JSON parse", "OKOKOKOKOKOKOKO");

		try {
			Log.i("JSON parse", "kkkkkkkkkkkkkkkkkkkkkkkk");
			String s = json.getString(TAG_CITY_ID);
			Log.i("JSON parse", s);
			
			//Get List array from json string 
			list = json.getJSONArray(TAG_LIST);
			
			
			//Loop over by increment by 24 = 24hrs to get value of next day 
			for(int i = 0; i < list.length(); i++)
			{
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject f = list.getJSONObject(i);
				
				JSONArray w = f.getJSONArray(TAG_WEATHER);
				
				//Get weather array from list 
				for(int j = 0; j < w.length(); j++)
				{
					JSONObject cl = w.getJSONObject(j);
				    
					String clouds = cl.getString(TAG_MAIN1);
					map.put(TAG_MAIN1,clouds);
				    Log.i("Inside Weather", clouds);
				    String desc = cl.getString(TAG_DESCRIPTION);
					map.put(TAG_DESCRIPTION,desc);
				
				}
				
				JSONObject m = f.getJSONObject(TAG_MAIN);
				
				//get Temperature and convert to degree
				Double t =  m.getDouble(TAG_TEMP) - 273.15 ;
				String temp = String.format("%.2f", t);
				Log.i("JSON temp", "got it"+t);
				Double tmin =  m.getDouble(TAG_TEMP_MIN ) - 273.15 ;
				String temp_min = String.format("%.2f", tmin);
				Log.i("JSON tmin", "got it too "+tmin);
				Double tmax =  m.getDouble(TAG_TEMP_MAX) - 273.15;
				String temp_max = String.format("%.2f", tmax);
				Log.i("JSON tmax", "got it "+tmax);
				
				
				//get pressure and humidity
				int p = m.getInt(TAG_PRESSURE);
				Log.i("JSON parse", "pressure: " + p );
				int humidity = m.getInt(TAG_HUMIDITY);
				Log.i("JSON parse","" + humidity);
				
				//get speed from wind obj
				JSONObject win = f.getJSONObject(TAG_WIND);
				String speed = win.getString(TAG_SPEED);
				
				//get unix timestamp and convert to date
				Long dt = f.getLong(TAG_DATE);
				Log.i("Date Extracted","unix" + dt);
				TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
				java.util.Date rTime= new java.util.Date((dt*1000));
				
				Log.i("Date Extracted2","date" + rTime );

				
				
				String pressure =" " + p;
				String humid =" " + humidity;
				
				map.put(TAG_DATE,rTime.toString());
				map.put(TAG_TEMP_MIN ,temp_min);
				map.put(TAG_TEMP_MAX,temp_max);
				map.put(TAG_TEMP,temp);
				map.put(TAG_PRESSURE, pressure);
				map.put(TAG_HUMIDITY, humid.toString());
				map.put(TAG_SPEED, speed);
				
				dateList.add(map);
			}
			
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		
		 // Update parsed data to ListView
		 
		ListAdapter adapter = new SimpleAdapter(this, dateList,
				R.layout.list_item,
				new String[] {  TAG_DATE, TAG_TEMP, TAG_MAIN1,TAG_HUMIDITY ,TAG_PRESSURE,TAG_SPEED
				               ,TAG_TEMP_MIN,TAG_TEMP_MAX,TAG_DESCRIPTION,}, new int[] {
						        R.id.daterTextView, R.id.temperature, R.id.weather ,R.id.humidTextView1,R.id.pressTextView1,
						        R.id.speedTextView1,R.id.minTextView1,R.id.maxTextview1,R.id.descTextView1});

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();

		// Launch new screen on Selection of  Single Item
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// getting values of the ListItem
				String main = ((TextView) view.findViewById(R.id.weather)).getText().toString();
				String press = ((TextView) view.findViewById(R.id.pressTextView1)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.descTextView1)).getText().toString();
				String date = ((TextView) view.findViewById(R.id.daterTextView)).getText().toString();
				String sp = ((TextView) view.findViewById(R.id.speedTextView1)).getText().toString();
				String tmi = ((TextView) view.findViewById(R.id.minTextView1)).getText().toString();
				String tma = ((TextView) view.findViewById(R.id.maxTextview1)).getText().toString();
				String humid = ((TextView) view.findViewById(R.id.humidTextView1)).getText().toString();
				
				// Starting intent
				Intent in = new Intent(getApplicationContext(), SingleActivity.class);
				
				in.putExtra(TAG_PRESSURE, press);
				in.putExtra(TAG_MAIN, main);
				in.putExtra(TAG_DESCRIPTION, description);
				in.putExtra(TAG_HUMIDITY, humid);
				in.putExtra(TAG_DATE, date);
				in.putExtra(TAG_SPEED, sp);
				in.putExtra(TAG_TEMP_MIN, tmi);
				in.putExtra(TAG_TEMP_MAX, tma);
				startActivity(in);

			}
		});
		
		

	}

}