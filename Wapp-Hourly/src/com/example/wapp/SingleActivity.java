package com.example.wapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleActivity  extends Activity {
	
	
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_MAIN1 = "main";
	private static final String TAG_PRESSURE = "pressure";
	private static final String TAG_HUMIDITY = "humidity";
	private static final String TAG_DATE = "dt";
	private static final String TAG_SPEED = "speed";
	private static final String TAG_TEMP_MIN = "temp_min";
	private static final String TAG_TEMP_MAX= "temp_max";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaldisplay);
        
     // Get values 
        Intent in = getIntent();
        
        
        String main = in.getStringExtra(TAG_MAIN1);
        String desc = in.getStringExtra(TAG_DESCRIPTION);
        String pressure = in.getStringExtra(TAG_PRESSURE);
        String humidity = in.getStringExtra(TAG_HUMIDITY);
        String date = in.getStringExtra(TAG_DATE);
        String sp = in.getStringExtra(TAG_SPEED);
        String tmi= in.getStringExtra(TAG_TEMP_MIN);
        String tma = in.getStringExtra(TAG_TEMP_MAX);
        setTitle(date);
        
        // Display
        
        TextView Pressure = (TextView) findViewById(R.id.pressureTextView);
        TextView Humidity = (TextView) findViewById(R.id.humidityTextView);
        TextView dt = (TextView) findViewById(R.id.dateTextView);
        TextView speed = (TextView) findViewById(R.id.speedTextView);
        TextView tMI = (TextView) findViewById(R.id.tempMinTextview);
        TextView tMA = (TextView) findViewById(R.id.tempMaxTextView);
        TextView de = (TextView) findViewById(R.id.mainTextView);
        TextView ma = (TextView) findViewById(R.id.descTextView);
       
        dt.setText(date);
        speed.append(" " + sp + "m/s");
        Pressure.append(" " + pressure + " hpa" );
        Humidity.append(" " + humidity + "%" );
        tMI.append(" " + tmi + "deg");
        tMA.append(" " + tma + "deg");
        ma.append(" " + main);
        de.append(" " + desc);
    }
}
