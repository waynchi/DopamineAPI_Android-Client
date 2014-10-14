package com.dopamine.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.provider.Settings.Secure;

import com.dopamine.api.DopamineRequest.Type;

/**
 * Created by pradeepbk4u on 4/18/14.
 */
public abstract class DopamineBase {
	
	static Context context = null;
	
	// Options
	private static boolean quickTrack = true;
	private static boolean memorySaverProcessorWaster = false;
	
	protected static boolean debugMode = true;
	
	// Data objects
	protected static String appID, key, token, versionID, build;
	private static LinkedHashSet<String> rewardFunctions = new LinkedHashSet<String>();
	private static LinkedHashSet<String> feedbackFunctions = new LinkedHashSet<String>();
	private static ArrayList<DopamineAction> actions = new ArrayList<DopamineAction>();
	private static HashMap<String, String> identity = new HashMap<String, String>();
	private static ArrayList<SimpleEntry<String, Object>> metaData = new ArrayList<SimpleEntry<String,Object>>();
	private static ArrayList<SimpleEntry<String, Object>> persistentMetaData = new ArrayList<SimpleEntry<String,Object>>();
	private static String clientOS = "Android";
	private static int clientOSversion = android.os.Build.VERSION.SDK_INT;
	private static String clientAPIversion = "1.2.0";

	// JSON field names --> NAME_dataType
	final static String CLIENTOS_string = "ClientOS";
	final static String CLIENTOSVERSION_int = "ClientOSVersion";
	final static String CLIENTAPIVERSION_string = "ClientAPIVersion";
	final static String IDENTITY_keyvaluearray = "identity";
	final static String KEY_string = "key";
	final static String TOKEN_string = "token";
	final static String VERSIONID_string = "versionID";
	final static String BUILD_string = "build";
	final static String EVENTNAME_string = "eventName";
	final static String LOCALTIME_long = "localTime";
	final static String UTC_long = "UTC";
	final static String METADATA_keyvaluearray = "metaData";
	final static String REWARDFUNCTIONS_stringarray = "rewardFunctions";
	final static String FEEDBACKFUNCTIONS_stringarray = "feedbackFunctions";
	final static String ACTIONPAIRINGS_jsonarray = "actionPairings";
	final static String ACTIONNAME_string = "actionName";
	final static String PAIREDFUNCTION_jsonarray = "reinforcers";
	final static String PAIREDFUNCTIONNAME_string = "functionName";
	final static String PAIREDFUNCTIONTYPE_string = "type";
	final static String PAIREDFUNCTIONCONSTRAINTS_stringarray = "constraint";
	final static String PAIREDFUNCTIONOBJECTIVES_stringarray = "objective";
	
	protected DopamineBase() {
	}

	protected static void initBase(Context c){
		if(context!=null)	// already been initialized
			return;
		
		context = c;
		if(identity == null) {
			identity = new HashMap<String, String>();
		}
		setDeviceID(c);
		
		DopamineRequest initRequest = new DopamineRequest(Type.Initilization, null);
		initRequest.execute();
		
		class testThread extends Thread {
			@Override
			public void run(){
				String t = "";
				for(int j = 0; j < 20; j ++){
					char c = 'A';
					for(int i = 0; i < 26; i++) {
						t+=c++;
						t = t.toLowerCase(Locale.ENGLISH);
					}
				}
				System.out.println(t);
			}
		};
		Thread t = new testThread();
		t.start();
		
		if(metaData != null) {
			metaData.clear();
		}
		
	}

	public static Object[] reinforce(DopamineAction action) {
		DopamineRequest dr = new DopamineRequest(Type.Reinforcement, getReinforceRequest(action.actionName));
		String resultFunction = null;
		try {
			dr.execute();
			dr.get();
			
			resultFunction = dr.resultFunction;
			if(metaData != null) {
				metaData.clear();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		Object[] toReturn = {resultFunction};
		return toReturn;
	}
	
	public static void track(String eventName) {
		if( !quickTrack ) {
			DopamineRequest.addTrackingRequest( getTrackRequest(eventName) );
		} else{
			new DopamineRequest(DopamineRequest.Type.Tracking, getTrackRequest(eventName)).execute();
		}
		
		if(metaData != null) {
			metaData.clear();
		}
	}
	
	public static int getTrackingQueueSize(){
		return DopamineRequest.getTrackingQueueSize();
	}
	
	public static void sendTrackingCalls(){
		new DopamineRequest(DopamineRequest.Type.Tracking, null).execute();
	}
	
	public static void setQuickTrack(boolean option){
		quickTrack = option;
		
		// if reset, send requests currently in queue
		if(quickTrack == true) {
			sendTrackingCalls();
		}
	}
	
	public static void setMemorySaver(boolean option){
		memorySaverProcessorWaster = option;
		DopamineRequest.setMemorySaverProcessorWaster(option);
	}
	
	public static boolean getMemorySaverState(){
		return memorySaverProcessorWaster;
	}

	// ///////////////////////////////////
	//
	// Request functions
	//
	// ///////////////////////////////////

	private static JSONObject getBaseRequest() {
		JSONObject jsonObject = new JSONObject();
		try {
			
			jsonObject.put(CLIENTOS_string, clientOS);
			jsonObject.put(CLIENTOSVERSION_int, clientOSversion);
			jsonObject.put(CLIENTAPIVERSION_string, clientAPIversion);
			jsonObject.put(KEY_string, key);
			jsonObject.put(TOKEN_string, token);
			jsonObject.put(VERSIONID_string, versionID);
			jsonObject.put(IDENTITY_keyvaluearray, hashMapToJSONArrayOfJSONObjects(identity));
			jsonObject.put(BUILD_string, build);
			

			long utcTime = System.currentTimeMillis();
			long localTime = utcTime + TimeZone.getDefault().getOffset(utcTime);
			utcTime/=1000;
			localTime/=1000;
			jsonObject.put(UTC_long, utcTime);
			jsonObject.put(LOCALTIME_long, localTime);


		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return jsonObject;
	}

	protected static JSONObject getInitRequest() {
		JSONObject jsonObject = getBaseRequest();
		
		if (jsonObject != null) {
			try {
				jsonObject.put(REWARDFUNCTIONS_stringarray, hashSetToJSONArray(rewardFunctions));
				jsonObject.put(FEEDBACKFUNCTIONS_stringarray, hashSetToJSONArray(feedbackFunctions));
				
				JSONArray actionPairings = new JSONArray();
				jsonObject.put(ACTIONPAIRINGS_jsonarray, actionPairings);
				
				for(DopamineAction action : actions){
					JSONObject actionPairingObject = new JSONObject();
					actionPairings.put(actionPairingObject);
					
					actionPairingObject.put(ACTIONNAME_string, action.actionName);
					JSONArray pairedFunctions = new JSONArray();
					actionPairingObject.put(PAIREDFUNCTION_jsonarray, pairedFunctions);
					
					for(String functionName : action.feedbackFunctions){
						JSONObject function = new JSONObject();
						function.put(PAIREDFUNCTIONNAME_string, functionName);
						function.put(PAIREDFUNCTIONTYPE_string, "Feedback");
						function.put(PAIREDFUNCTIONOBJECTIVES_stringarray, new JSONArray());
						function.put(PAIREDFUNCTIONCONSTRAINTS_stringarray, new JSONArray());
						pairedFunctions.put(function);
					}
					for(String functionName : action.rewardFunctions){
						JSONObject function = new JSONObject();
						function.put(PAIREDFUNCTIONNAME_string, functionName);
						function.put(PAIREDFUNCTIONTYPE_string, "Reward");
						function.put(PAIREDFUNCTIONOBJECTIVES_stringarray, new JSONArray());
						function.put(PAIREDFUNCTIONCONSTRAINTS_stringarray, new JSONArray());
						pairedFunctions.put(function);
					}
					
				}
				
				
				if(debugMode) {
					System.out.println("\nInit request: " + jsonObject.toString(2));
				}

			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			// Error
		}
		return jsonObject;
	}

	private static JSONObject getTrackRequest(String eventName) {
		JSONObject jsonObject = getBaseRequest();

		if (jsonObject != null) {
			try {
				jsonObject.put(EVENTNAME_string, eventName);
				jsonObject.put(METADATA_keyvaluearray, simpleEntryListToJSONArray(metaData));
				jsonObject.accumulate(METADATA_keyvaluearray, simpleEntryListToJSONArray(persistentMetaData));
				
				if(debugMode) {
					System.out.println("\nTracking Request: " + jsonObject.toString(2));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			// Error
		}

		return jsonObject;
	}

	private static JSONObject getReinforceRequest(String eventName) {
		JSONObject jsonObject = getBaseRequest();

		if (jsonObject != null) {
			try {
				jsonObject.put(EVENTNAME_string, eventName);
				jsonObject.put(METADATA_keyvaluearray, simpleEntryListToJSONArray(metaData));
				jsonObject.accumulate(METADATA_keyvaluearray, simpleEntryListToJSONArray(persistentMetaData));
				
				if(debugMode) {
					System.out.println("\nReinforcement Request: " + jsonObject.toString(2));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			// Error
		}

		
		return jsonObject;
	}

	// Request helper functions
	// //////////////////////////
	private static JSONArray listToJSONArray(ArrayList<String> list) {
		JSONArray array = new JSONArray();
		for (String s : list) {
			array.put(s);
		}
		return array;
	}
	
	private static JSONArray hashSetToJSONArray(HashSet<String> set) {
		JSONArray array = new JSONArray();
		for (String s : set) {
			array.put(s);
		}
		return array;
	}
	
	private static JSONArray hashMapToJSONArrayOfJSONObjects(HashMap<String, String> map){
		JSONArray array = new JSONArray();
		for(Entry<String, String> entry : map.entrySet()){
			JSONObject object = new JSONObject();
			try {
				object.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			array.put(object);
		}
		return array;
	}
	
//	private static JSONArray hashedSimpleEntrySetToJSONArray(LinkedHashSet<SimpleEntry<String, String>> set) {
//		JSONArray array = new JSONArray();
//		for (String s : set)
//			array.put(s);
//		return array;
//	}

	private static JSONArray simpleEntryListToJSONArray(ArrayList<SimpleEntry<String, Object>> list) {
		// create JSONObject to combine entries with identical keys into a single JSONObject inside of the JSONArray
		JSONObject obj = new JSONObject();
		try {

			for (SimpleEntry<String, Object> entry : list) {
				obj.accumulate(entry.getKey(), entry.getValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONArray array = new JSONArray();
		Iterator<String> it = obj.keys();
		while (it.hasNext()) {
			try {
				String[] name = { it.next() };
				// create individual objects for each metadata key
				array.put(new JSONObject(obj, name));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return array;
	}

	//////////////////////////////////////
	//
	// Setter functions
	//
	//////////////////////////////////////

	protected static String setBuild() {
		StringBuilder builder = new StringBuilder("pairings:");
		
		for(DopamineAction action : actions){
			builder.append(action.actionName);
			
			for (String feedback : action.feedbackFunctions) {
				builder.append(feedback);
			}

			for (String reward : action.rewardFunctions) {
				builder.append(reward);
			}
		}
		build = sha1(builder.toString());
		return build;
	}
	public static String sha1(String s) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.reset();
		byte[] data = digest.digest( s.getBytes() );
		return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,
				data));
	}

	static void addRewardFunctions(String... names) {
		for(String name : names) {
			rewardFunctions.add(name);
		}
	}

	static void addFeedbackFunctions(String... names) {
		for(String name : names) {
			feedbackFunctions.add(name);
		}
	}
	
	static void addAction(DopamineAction a){
		actions.add(a);
	}

	protected static void setIdentity(String IDType, String uniqueID) {
		if (identity == null) {
			identity = new HashMap<String, String>();
		}

		identity.put( IDType, uniqueID );
	}
	protected static String clearIdentity(String IDType){
		if(identity == null)
			return null;
		
		return identity.remove(IDType);
	}

	public static void addMetaData(String key, Object value) {
		if (metaData == null) {
			metaData = new ArrayList<SimpleEntry<String, Object>>();
		}

		clearMetaData(key);
		metaData.add(new SimpleEntry<String, Object>(key, value));
	}
	public static void clearMetaData(String key){
		if(metaData == null) return;
		
		for(int i = 0; i < metaData.size(); i ++){
			SimpleEntry<String, Object> entry = metaData.get(i);
			if(entry.getKey().equalsIgnoreCase(key)){
				metaData.remove(i);
				return;
			}
		}
	}

	public static void addPersistentMetaData(String key, Object value) {
		if (persistentMetaData == null) {
			persistentMetaData = new ArrayList<SimpleEntry<String,Object>>();
		}

		clearPersistentMetaData(key);
		persistentMetaData.add(new SimpleEntry<String, Object>(key, value));
	}
	public static void clearPersistentMetaData(String key){
		if (persistentMetaData == null)
			return;
		
		for(int i = 0; i < persistentMetaData.size(); i++){
			SimpleEntry<String, Object> entry = persistentMetaData.get(i);
			if( entry.getKey().equalsIgnoreCase(key)){
				persistentMetaData.remove(i);
				return;
			}
		}
	}
	
	
	//////////////////////////////////////
	//
	// Setter Helper functions
	//
	//////////////////////////////////////
	
	
	protected static void setDeviceID(Context c) {
		
		String android_id = Secure.getString(c.getContentResolver(),
                Secure.ANDROID_ID);
		
		identity.put("DEVICE_ID", android_id);
	}
}
