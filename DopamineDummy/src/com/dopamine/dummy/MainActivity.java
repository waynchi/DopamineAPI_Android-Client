package com.dopamine.dummy;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		
		MyDopamine.init(context);
		MyDopamine.setQuickTrack(false);  // optional
//		MyDopamine.setMemorySaver(true);  // optional
		
		// Example of Persistent MetaData
		String persistentData = null;
		switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
		case 1: persistentData = "Sunday"; break;
		case 2: persistentData = "Monday"; break;
		case 3: persistentData = "Tuesday"; break;
		case 4: persistentData = "Wednesday"; break;
		case 5: persistentData = "Thursday"; break;
		case 6: persistentData = "Friday"; break;
		default:
			persistentData = "Saturday"; break;
		}
		MyDopamine.addPersistentMetaData("Day", persistentData);
		
		// Example of MetaData
		MyDopamine.addMetaData("firstMetaData", 0);
		
		final Button reinforcementButton = (Button) findViewById(R.id.reinforecement_button);
		reinforcementButton.setOnClickListener(new View.OnClickListener() {
			int i = 0;

			public void onClick(View v) {
				// Example of MetaData
				MyDopamine.addMetaData("reinforcementNumber", i);
				String result = MyDopamine.clickReinforcementButton.reinforce();
				
				if(result.equals(MyDopamine.FEEDBACKFUNCTION1)){
					reinforcementButton.setBackgroundColor(Color.BLUE);
					MainActivity.feedBackFunction1();
				} 
				else if(result.equals(MyDopamine.FEEDBACKFUNCTION2)){
					reinforcementButton.setBackgroundColor(Color.BLUE);
					MainActivity.feedBackFunction2();
				}
				else if(result.equalsIgnoreCase(MyDopamine.FEEDBACKFUNCTION3)){
					reinforcementButton.setBackgroundColor(Color.BLUE);
					MainActivity.feedBackFunction3();
				}
				else if(result.equalsIgnoreCase(MyDopamine.REWARDFUNCTION1)){
					reinforcementButton.setBackgroundColor(Color.RED);
					MainActivity.rewardFunction1();
				}
				else if(result.equalsIgnoreCase(MyDopamine.REWARDFUNCTION2)){
					reinforcementButton.setBackgroundColor(Color.RED);
					MainActivity.rewardFunction2();
				}
				
				
				
				reinforcementButton.setText("Reinforcement " + i++);
			}
		});
		
		final Button trackingButton = (Button) findViewById(R.id.tracking_button);
		trackingButton.setOnClickListener(new View.OnClickListener() {
			int i = 0;

			public void onClick(View v) {
				// Example of MetaData
				MyDopamine.addMetaData("trackingNumber", i);
				MyDopamine.track("track button pushed");
				trackingButton.setText("Track " + i++);
				
				System.out.println("Tracking queue size: " + MyDopamine.getTrackingQueueSize());
				if(MyDopamine.getTrackingQueueSize() > 10)
					MyDopamine.sendTrackingCalls();
			}
		});
		
		final TextView textView = (TextView) findViewById(R.id.reinforecement_message);
		textView.setText("Red=reward, Blue=feedback");
		textView.setTextColor(Color.BLACK);
		
	}
	
	public static void feedBackFunction1(){
		Toast.makeText(context, "feedBackFunction1", Toast.LENGTH_SHORT).show();
	}
	public static void feedBackFunction2(){
		Toast.makeText(context, "feedBackFunction2", Toast.LENGTH_SHORT).show();
	}
	public static void feedBackFunction3(){
		Toast.makeText(context, "feedBackFunction3", Toast.LENGTH_SHORT).show();
	}
	
	public static void rewardFunction1(){
		Toast.makeText(context, "rewardFunction1", Toast.LENGTH_SHORT).show();
	}
	public static void rewardFunction2(){
		Toast.makeText(context, "rewardFunction2", Toast.LENGTH_SHORT).show();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
