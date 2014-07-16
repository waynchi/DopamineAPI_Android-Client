package com.dopamine.dummy;

import java.io.IOException;

import com.dopamine.api.Dopamine;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		
		// signed up info on akashdes@usc.edu
				Dopamine.setIdentity("email", "akashdes@usc.edu");
				Dopamine.setIdentity("mac", "AB:CD:EF:GH:IJ");
				Dopamine.setAppID("53bf3dfbf572f3b63ee628de");
				Dopamine.setToken("493245694786310253bf3dfbf572f3b63ee628de");
				Dopamine.setKey("db07887eec605bff3a9ae5ae5374152ced642ed5");
				Dopamine.setVersionID("exampleApp2");
		        

				Dopamine.addRewardFunctions("rewardFunction1");
				Dopamine.addRewardFunctions("rewardFunction2");


				Dopamine.addFeedbackFunctions("feedBackFunction1");
				Dopamine.addFeedbackFunctions("feedBackFunction2");
				Dopamine.addFeedbackFunctions("feedbackFunction3");
		        
				try {
					Dopamine.init(this.getApplicationContext());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		final Button reinforcementButton = (Button) findViewById(R.id.reinforecement_button);
		reinforcementButton.setOnClickListener(new View.OnClickListener() {
			int i = 0;

			public void onClick(View v) {
				String result = Dopamine.reinforce("reinforcedBehavior");
//				String result = Dopamine.result;
				
				System.out.println("MainActivity result: " + result);
				if(result.equals("feedBackFunction1")){
					reinforcementButton.setBackgroundColor(Color.BLUE);
					MainActivity.feedBackFunction1();
				} 
				else if(result.equals("feedBackFunction2")){
					reinforcementButton.setBackgroundColor(Color.BLUE);
					MainActivity.feedBackFunction2();
				}
				else if(result.equalsIgnoreCase("rewardFunction1")){
					reinforcementButton.setBackgroundColor(Color.RED);
					MainActivity.rewardFunction1();
				}
				else if(result.equalsIgnoreCase("rewardFunction2")){
					reinforcementButton.setBackgroundColor(Color.RED);
					MainActivity.rewardFunction2();
				}
				else if(result.equalsIgnoreCase("rewardFunction3")){
					reinforcementButton.setBackgroundColor(Color.RED);
					MainActivity.rewardFunction3();
				}
				
				
				reinforcementButton.setText("Reinforcement " + i++);
			}
		});
		
		final Button trackingButton = (Button) findViewById(R.id.tracking_button);
		trackingButton.setOnClickListener(new View.OnClickListener() {
			int i = 0;

			public void onClick(View v) {
				Dopamine.track("track button pushed");
				trackingButton.setText("Track " + i++);
			}
		});
		
		final TextView textView = (TextView) findViewById(R.id.reinforecement_message);
		textView.setText("Red=reward, Blue=feedback");
		
		
	}
	
	public static void feedBackFunction1(){
		Toast.makeText(context, "feedBackFunction1", Toast.LENGTH_SHORT).show();
	}
	public static void feedBackFunction2(){
		Toast.makeText(context, "feedBackFunction2", Toast.LENGTH_SHORT).show();
	}
	
	public static void rewardFunction1(){
		Toast.makeText(context, "rewardFunction1", Toast.LENGTH_SHORT).show();
	}
	public static void rewardFunction2(){
		Toast.makeText(context, "rewardFunction2", Toast.LENGTH_SHORT).show();
	}
	public static void rewardFunction3(){
		Toast.makeText(context, "rewardFunction3", Toast.LENGTH_SHORT).show();
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
