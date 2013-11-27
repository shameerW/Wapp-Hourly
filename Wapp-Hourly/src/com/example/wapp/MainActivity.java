package com.example.wapp;

import java.util.Arrays;

import com.example.wapp.ParsingActivity;
import com.example.wapp.MainActivity;
import com.example.wapp.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	//  unique message using  package name to avoid conflicts with other applications
	
	public final static String CITY_SYMBOL = "com.example.myfirstapp.city";
	
	// Manage key valued pairs 
	private SharedPreferences citySymbolsEntered;
	
	// Table inside the scroll view 
	private TableLayout cityTableScrollView;
	
	
	private EditText citySymbolEditText;
	

	Button enterCitySymbolButton;
	Button deleteCitiesButton;

	// Set up the activity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Retrieve saved cities previously entered  
		
		citySymbolsEntered = getSharedPreferences("cityList", MODE_PRIVATE);
		
		// Initialize 
		cityTableScrollView = (TableLayout) findViewById(R.id.cityTableScrollView);
		citySymbolEditText = (EditText) findViewById(R.id.cityEditText);
		enterCitySymbolButton = (Button) findViewById(R.id.goButton);
		deleteCitiesButton = (Button) findViewById(R.id.deleteCitiesButton);
		
		//ClickListeners 
		enterCitySymbolButton.setOnClickListener(entercityButtonListener);
		deleteCitiesButton.setOnClickListener(deleteCitiesButtonListener);
		
		// Add saved cities to the city Scrollview
		updateSavedcityList(null);
	}
	
	// Either adds a new city 
	private void updateSavedcityList(String newCitySymbol){
		
		// Get the saved cities
		String[] cities = citySymbolsEntered.getAll().keySet().toArray(new String[0]);
		
		// Sort the cities 
		Arrays.sort(cities, String.CASE_INSENSITIVE_ORDER);
		
		
		if(newCitySymbol != null){
			
			// Enter the new city in sorted order 
			insertcityInScrollView(newCitySymbol, Arrays.binarySearch(cities, newCitySymbol));
			
		} else {
			
			// Display
			for(int i = 0; i < cities.length; ++i){
				
				insertcityInScrollView(cities[i], i);
				
			}
			
		}
		
	}
	
	private void savecitiesymbol(String newcity){
		
		// Used to check if this is a new City
		String isThecityNew = citySymbolsEntered.getString(newcity, null);
		
		// Editor is used to store a key / value pair
		
		SharedPreferences.Editor preferencesEditor = citySymbolsEntered.edit();
		preferencesEditor.putString(newcity, newcity);
		preferencesEditor.apply();
		
		// If this is a new city then add components
		if(isThecityNew == null){
			updateSavedcityList(newcity);
		}
		
	}
	
	private void insertcityInScrollView(String city, int arrayIndex){
		
		// Get the LayoutInflator service
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Use the inflater to inflate a city row from city_row.xml
		View newcityRow = inflater.inflate(R.layout.city_row, null);
		
		// Create the TextView for the ScrollView Row
		TextView newcityTextView = (TextView) newcityRow.findViewById(R.id.cityTextView);
		
		// Add the city to the TextView
		newcityTextView.setText(city);
		
		Button cityQuoteButton = (Button) newcityRow.findViewById(R.id.historyButton);
		cityQuoteButton.setOnClickListener(getcityActivityListener);
		
		Button quoteFromWebButton = (Button) newcityRow.findViewById(R.id.gpsButton);
		quoteFromWebButton.setOnClickListener(getcityFromWebsiteListener);
		
		// Add the new components for the city to the TableLayout
		cityTableScrollView.addView(newcityRow, arrayIndex);
		
	}
	
	public OnClickListener entercityButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			// If there is a city symbol entered into the EditText
			
			 String Breaker = citySymbolEditText.getText().toString();
			if(Breaker.length() > 0){
				
				String [] array = Breaker.split(",");
				// Save the new city and add its components
				for(int j = 0;j<array.length;j++)
				{			
				  savecitiesymbol(array[j]);
				
				   citySymbolEditText.setText(""); // Clear EditText 
				}
				// Force the keyboard after button clicked
				    InputMethodManager imm = (InputMethodManager)getSystemService(
					         Context.INPUT_METHOD_SERVICE);
					  imm.hideSoftInputFromWindow(citySymbolEditText.getWindowToken(), 0);
			} else {
				
				//Alert dialog box
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				
				
				builder.setTitle(R.string.invalid);
				
				
				builder.setPositiveButton(R.string.ok, null);
				
				
				builder.setMessage(R.string.ok);
				
				
				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();
				
			}
            
			
		}
		
	};
	
	private void deleteAllcities(){
		
		// Delete all the cities stored in the TableLayout
		cityTableScrollView.removeAllViews();
		
	}
	
	public OnClickListener deleteCitiesButtonListener = new OnClickListener(){


		public void onClick(View v) {
			
			deleteAllcities();
			
			// Editor is used to store a key / value pairs
			
			SharedPreferences.Editor preferencesEditor = citySymbolsEntered.edit();
			
			// deleting the key / value pairs
			preferencesEditor.clear();
			preferencesEditor.apply();
			
		}
		
	};

	public OnClickListener getcityFromWebsiteListener = new OnClickListener(){

		public void onClick(View v) {
			
			// Get the text saved in the respective TextView of the clicked button
			// with the id citiesymbolTextView

			TableRow tableRow = (TableRow) v.getParent();
            TextView cityTextView = (TextView) tableRow.findViewById(R.id.cityTextView);
            String citiesymbol = cityTextView.getText().toString();
            
            // The URL specific for the city symbol
            String cityURL = getString(R.string.searchURL) + citiesymbol;
            
            Intent getcityWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(cityURL));
            
            startActivity(getcityWebPage);
			
		}
		
	};
	
	public OnClickListener getcityActivityListener = new OnClickListener(){

		public void onClick(View v) {
			
			if (isNetworkConnected()==true)
		    {
				TableRow tableRow = (TableRow) v.getParent();
				TextView cityTextView = (TextView) tableRow.findViewById(R.id.cityTextView);
		        String city_sent = cityTextView.getText().toString();
		        Log.i("CITY SENT", city_sent);
		            
		            
		        Intent intent = new Intent(MainActivity.this, ParsingActivity.class);
		            
		       // Add the city to  intent
		       intent.putExtra(CITY_SYMBOL, city_sent);
		            
		       startActivity(intent);
		    }
			else
			{
				//Alert dialog box
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				
				 
				builder.setTitle(R.string.connectionE);
				
				
				builder.setPositiveButton(R.string.ok, null);
				
				
				builder.setMessage(R.string.connection);
				
				
				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();
				
			}
			
		}
		
	};
	
	 private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {
		   // There are no active networks.
		   return false;
		  } else
		   return true;
		 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
