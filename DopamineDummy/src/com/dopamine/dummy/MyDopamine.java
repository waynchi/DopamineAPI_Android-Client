package com.dopamine.dummy;

import android.content.Context;

import com.dopamine.api.DopamineAction;
import com.dopamine.api.DopamineBase;

public class MyDopamine extends DopamineBase {
	
	// Declare Actions with their ActionNames
	public static final DopamineAction clickReinforcementButton  = new DopamineAction("reinforcedBehavior");
	
	// Declare Feedback Function names
	public static final String FEEDBACKFUNCTION1 = "feedBackFunction1";
	public static final String FEEDBACKFUNCTION2 = "feedBackFunction2";
	public static final String FEEDBACKFUNCTION3 = "feedbackFunction3";
	
	// Declare Reward Function names
	public static final String REWARDFUNCTION1 = "rewardFunction1";
	public static final String REWARDFUNCTION2 = "rewardFunction2";
	
	
	public static void init(Context c){
//		Debug.startMethodTracing("DopamineInitTest7");
		// Set Credentials
		appID = "53bf3dfbf572f3b63ee628de";
		versionID = "DopamineDummy5";
		key = "db07887eec605bff3a9ae5ae5374152ced642ed5";
		token = "493245694786310253bf3dfbf572f3b63ee628de";
		
		// Pair Actions to Feedback Functions
		clickReinforcementButton.pairFeedback(FEEDBACKFUNCTION1);
		clickReinforcementButton.pairFeedback(FEEDBACKFUNCTION2);
		clickReinforcementButton.pairFeedback(FEEDBACKFUNCTION3);

		// Pair Actions to Reward Functions
		clickReinforcementButton.pairReward(REWARDFUNCTION1);
		clickReinforcementButton.pairReward(REWARDFUNCTION2);
//
//		// Options
////		setQuickTrack(false);	// default: true. if false, must use sendTrackingCalls() to manually send calls
////		setMemorySaver(true);	// default: false
		
		
		// Initialize base
		initBase(c);
//		Debug.stopMethodTracing();

	}
}
