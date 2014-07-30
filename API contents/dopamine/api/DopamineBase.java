package com.dopamine.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

/**
 * Created by pradeepbk4u on 4/18/14.
 */
public abstract class DopamineBase {
	
	static Context context;
	
	// Options
	private static boolean quickTrack = true;
	private static boolean memorySaverProcessorWaster = false;
	
	static boolean debugMode = false;
	
	// Data objects
	protected static String appID, key, token, versionID, build;
	private static LinkedHashSet<String> rewardFunctions = new LinkedHashSet<String>();
	private static LinkedHashSet<String> feedbackFunctions = new LinkedHashSet<String>();
	private static ArrayList<DopamineAction> actions = new ArrayList<DopamineAction>();
	private static HashMap<String, String> identity = new HashMap<String, String>();
	private static ArrayList<SimpleEntry<String, Object>> metaData = new ArrayList<SimpleEntry<String,Object>>();
	private static ArrayList<SimpleEntry<String, Object>> persistentMetaData = new ArrayList<SimpleEntry<String,Object>>();
	private static String clientOS = "Android";
	private static String clientOSversion = android.os.Build.VERSION.RELEASE;
	private static String clientAPIversion = "1.1.0";

	// JSON field names --> NAME_dataType
	
	final static String CLIENTOS_string = "ClientOS";
	final static String CLIENTOSVERSION_string = "ClientOSVersion";
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
	final static String ACTIONNAME_string = "name";
	final static String PAIREDFUNCTION_jsonarray = "pairing";
	final static String PAIREDFUNCTIONNAME_string = "functionName";
	final static String PAIREDFUNCTIONTYPE_string = "type";
	final static String PAIREDFUNCTIONCONSTRAINTS_stringarray = "constraint";
	final static String PAIREDFUNCTIONOBJECTIVES_stringarray = "objective";
	
	protected DopamineBase() {
	}

	protected static void initBase(Context c){
		context = c;
		if(identity == null){
			identity = new HashMap<String, String>();
		}
		identity.put( "DEVICE_ID", getDeviceID() );
		setBuild();
		
		URIBuilder uri = new URIBuilder(appID);
		DopamineRequest initRequest = new DopamineRequest();
		try {
			initRequest.execute(uri.getURI(URIBuilder.URI.INIT), getInitRequest());
			initRequest.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if(metaData != null) metaData.clear();
	}

	public static Object[] reinforce(DopamineAction action) {
		URIBuilder uri = new URIBuilder(appID);
		DopamineRequest dr = new DopamineRequest();
		String resultFunction = null;
		Object[] arguments = null;
		try {
			dr.execute(uri.getURI(URIBuilder.URI.REWARD), getReinforceRequest(action.actionName));
			dr.get();
			
			resultFunction = dr.resultFunction;
			arguments = metaData.toArray();
			if(metaData != null) metaData.clear();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		Object[] toReturn = {resultFunction, arguments};
		return toReturn;
	}
	
	public static void track(String eventName) {
		if( !quickTrack ){
			DopamineRequest.addTrackingRequest( getTrackRequest(eventName) );
		}
		else{
			URIBuilder uri = new URIBuilder(appID);
			new DopamineRequest(DopamineRequest.Type.Tracking).execute(uri.getURI(URIBuilder.URI.TRACK), getTrackRequest(eventName));
		}
		
		if(metaData != null) metaData.clear();
	}
	
	public static int getTrackingQueueSize(){
		return DopamineRequest.getTrackingQueueSize();
	}
	
	public static void sendTrackingCalls(){
		URIBuilder uri = new URIBuilder(appID);
		new DopamineRequest(DopamineRequest.Type.Tracking).execute(uri.getURI(URIBuilder.URI.TRACK));
	}
	
	public static void setQuickTrack(boolean option){
		quickTrack = option;
		
		// if reset, send requests currently in queue
		if(quickTrack == true)
			sendTrackingCalls();
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
			jsonObject.put(CLIENTOSVERSION_string, clientOSversion);
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

	private static String getInitRequest() {
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
						function.put(PAIREDFUNCTIONTYPE_string, "feedback");
						function.put(PAIREDFUNCTIONOBJECTIVES_stringarray, new JSONArray());
						function.put(PAIREDFUNCTIONCONSTRAINTS_stringarray, new JSONArray());
						pairedFunctions.put(function);
					}
					for(String functionName : action.rewardFunctions){
						JSONObject function = new JSONObject();
						function.put(PAIREDFUNCTIONNAME_string, functionName);
						function.put(PAIREDFUNCTIONTYPE_string, "feedback");
						function.put(PAIREDFUNCTIONOBJECTIVES_stringarray, new JSONArray());
						function.put(PAIREDFUNCTIONCONSTRAINTS_stringarray, new JSONArray());
						pairedFunctions.put(function);
					}
					
				}
				
				
				if(debugMode) System.out.println("\nInit request: " + jsonObject.toString(2));

			} catch (JSONException e) {
				e.printStackTrace();
				return "0";
			}
		} else {
			// Error
		}
		return jsonObject.toString();
	}

	private static String getTrackRequest(String eventName) {
		JSONObject jsonObject = getBaseRequest();

		if (jsonObject != null) {
			try {
				jsonObject.put(EVENTNAME_string, eventName);
				jsonObject.put(METADATA_keyvaluearray, simpleEntryListToJSONArray(metaData));
				jsonObject.accumulate(METADATA_keyvaluearray, simpleEntryListToJSONArray(persistentMetaData));
				
				if(debugMode) System.out.println("\nTracking Request: " + jsonObject.toString(2));
			} catch (JSONException e) {
				e.printStackTrace();
				return "0";
			}
		} else {
			// Error
		}

		return jsonObject.toString();
	}

	private static String getReinforceRequest(String eventName) {
		JSONObject jsonObject = getBaseRequest();

		if (jsonObject != null) {
			try {
				jsonObject.put(EVENTNAME_string, eventName);
				jsonObject.put(METADATA_keyvaluearray, simpleEntryListToJSONArray(metaData));
				jsonObject.accumulate(METADATA_keyvaluearray, simpleEntryListToJSONArray(persistentMetaData));
				
				if(debugMode) System.out.println("\nReinforcement Request: " + jsonObject.toString(2));
			} catch (JSONException e) {
				e.printStackTrace();
				return "0";
			}
		} else {
			// Error
		}

		
		return jsonObject.toString();
	}

	// Request helper functions
	// //////////////////////////
	private static JSONArray listToJSONArray(ArrayList<String> list) {
		JSONArray array = new JSONArray();
		for (String s : list)
			array.put(s);
		return array;
	}
	
	private static JSONArray hashSetToJSONArray(HashSet<String> set) {
		JSONArray array = new JSONArray();
		for (String s : set)
			array.put(s);
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

	private static String setBuild() {
		StringBuilder builder = new StringBuilder();
		
		for (String reward : rewardFunctions) {
			builder.append(reward);
		}

		for (String feedback : feedbackFunctions) {
			builder.append(feedback);
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
		byte[] data = digest.digest(s.getBytes());
		return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,
				data));
	}

	static void addRewardFunctions(String... names) {
		for(String name : names)
			rewardFunctions.add(name);
	}

	static void addFeedbackFunctions(String... names) {
		for(String name : names)
			feedbackFunctions.add(name);
	}
	
	static void addAction(DopamineAction a){
		actions.add(a);
	}

	protected static void setIdentity(String IDType, String uniqueID) {
		if (identity == null)
			identity = new HashMap<String, String>();

		identity.put( IDType, uniqueID );
	}
	protected static String clearIdentity(String IDType){
		if(identity == null)
			return null;
		
		return identity.remove(IDType);
	}

	public static void addMetaData(String key, Object value) {
		if (metaData == null)
			metaData = new ArrayList<SimpleEntry<String, Object>>();

		metaData.add(new SimpleEntry<String, Object>(key, value));
	}

	public static void addPersistentMetaData(String key, Object value) {
		if (persistentMetaData == null)
			persistentMetaData = new ArrayList<SimpleEntry<String,Object>>();

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
	
	
	protected static String getDeviceID() {

		/*String Return_DeviceID = USERNAME_and_PASSWORD.getString(DeviceID_key,"Guest");
		return Return_DeviceID;*/

		TelephonyManager TelephonyMgr = (TelephonyManager) context.getApplicationContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId(); // Requires
		// READ_PHONE_STATE

		// 2 compute DEVICE ID
		String m_szDevIDShort = "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10
				+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
				+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
				+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
				+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
				+ Build.USER.length() % 10; // 13 digits
		// 3 android ID - unreliable
		String m_szAndroidID = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
		// 4 wifi manager, read MAC address - requires
		// android.permission.ACCESS_WIFI_STATE or comes as null
		WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		// 5 Bluetooth MAC address android.permission.BLUETOOTH required
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = m_BluetoothAdapter.getAddress();
		System.out.println("m_szBTMAC "+m_szBTMAC);

		// 6 SUM THE IDs
		String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID+ m_szWLANMAC + m_szBTMAC;
		System.out.println("m_szLongID "+m_szLongID);
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
		byte p_md5Data[] = m.digest();

		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		}
		m_szUniqueID = m_szUniqueID.toUpperCase(Locale.ENGLISH);

		return m_szUniqueID;

	}
}
