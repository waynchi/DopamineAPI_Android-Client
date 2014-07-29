package com.dopamine.api;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class DopamineAction {
	public final String actionName;
	public String resultFunction;
	public Object[] arguments;
	LinkedHashSet<String> rewardFunctions = new LinkedHashSet<String>();
	LinkedHashSet<String> feedbackFunctions = new LinkedHashSet<String>();
	
	
	public DopamineAction(String name){
		actionName = name;
		DopamineBase.addAction(this);
	}
	
	
	public void pairFeedback(String function){
		feedbackFunctions.add(function);
		DopamineBase.addFeedbackFunctions(function);
	}
	
	public void pairReward(String function){
		rewardFunctions.add(function);
		DopamineBase.addRewardFunctions(function);
	}
	
	public String reinforce() {
		Object[] resultAndArguments = DopamineBase.reinforce(this);
		resultFunction = (String) resultAndArguments[0];
		arguments = (Object[]) resultAndArguments[1];
		
		if(resultFunction.equals(DopamineRequest.NO_CONNECTION))
			resultFunction = getDefaultFeedbackFunction();
		
		return resultFunction;
	}
	
	LinkedList<String> getRewardFunctions(){
		LinkedList<String> rf = new LinkedList<String>();
		for(String s : rewardFunctions)
			rf.add(s);
		Collections.sort(rf);
		
		return rf;
	}
	
	LinkedList<String> getFeedbackFunctions(){
		LinkedList<String> ff = new LinkedList<String>();
		for(String s : feedbackFunctions)
			ff.add(s);
		Collections.sort(ff);
		
		return ff;
	}
	
	String getDefaultFeedbackFunction(){
		for(String s : feedbackFunctions){
			return s;
		}
		
		// if no functions found
		return null;
	}
	
}
