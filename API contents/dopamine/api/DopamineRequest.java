package com.dopamine.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DopamineRequest extends AsyncTask<String, Void, String> {
	public static final String NO_CONNECTION = "no internet connection";
	private static boolean memorySaverProcessorWaster = DopamineBase.getMemorySaverState();
	public static enum Type {Tracking, Other};
	Type type;
	
	private static final int timeoutConnection = 2000;
	private static final int timeoutSocket = 5000;
	
	public String resultFunction, resultString, error = "", status;
	public JSONArray arguments;
	private static int trackingQueueSize = 0;
	private static ReentrantLock trackingQueueLock = new ReentrantLock();
	
	private static Queue<String> trackingQueue = null;

	public DopamineRequest() {
		type = Type.Other;
	}
	public DopamineRequest(Type t){
		type = t;
		if(!memorySaverProcessorWaster && trackingQueue==null)
			trackingQueue = FileManager.loggedTrackingRequestsTOqueue();
	}
	
	static void setMemorySaverProcessorWaster(boolean option){
		memorySaverProcessorWaster = option;
		if(memorySaverProcessorWaster)
			trackingQueue = null;
		else
			trackingQueue = FileManager.loggedTrackingRequestsTOqueue();
	}
	
	static int getTrackingQueueSize(){
		return trackingQueueSize;
	}

	@Override
	protected String doInBackground(String... params) {
		
		SSLContext ctx = null;
		
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { new CustomX509TrustManager() }, new SecureRandom());

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			
			
			SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));

			if(type==Type.Tracking){
				// Tracking Call
				trackingQueueLock.lock();
				if(memorySaverProcessorWaster) trackingQueue = FileManager.loggedTrackingRequestsTOqueue();
				if(params.length!=1) trackingQueue.add(params[1]);
				while( !trackingQueue.isEmpty() ){
					String jsonRequest = trackingQueue.peek();
					boolean success = processHTTPpost(httpClient, params[0], jsonRequest);
					if(success) trackingQueue.poll();
					else{
						// quit
						FileManager.overwriteTrackingRequestLog(trackingQueue);
						trackingQueueSize = trackingQueue.size();
						if(memorySaverProcessorWaster) trackingQueue = null;
						trackingQueueLock.unlock();
						return resultFunction;	// not sure what to return, so reverted to default
					}
				}
				// successfully sent all requests. overwrite with empty queue
				FileManager.overwriteTrackingRequestLog(trackingQueue);
				trackingQueueSize = trackingQueue.size();
				if(memorySaverProcessorWaster) trackingQueue = null;
				trackingQueueLock.unlock();
			}
			else{
				// Reinforcement call
				boolean success = processHTTPpost(httpClient, params[0], params[1]);
				if(!success) {
					resultFunction = NO_CONNECTION;
					return NO_CONNECTION;
				}
			}

		} catch (Exception e) {
		}


		return resultFunction;
    }
	
	static void addTrackingRequest(String trackingJSONrequest){
		trackingQueueLock.lock();
		if(trackingQueue == null)
			trackingQueue = new LinkedList<String>();
		if(memorySaverProcessorWaster)
			trackingQueue = FileManager.loggedTrackingRequestsTOqueue();
		
		trackingQueue.add(trackingJSONrequest);
		trackingQueueSize = trackingQueue.size();
		FileManager.overwriteTrackingRequestLog(trackingQueue);
		
		if(memorySaverProcessorWaster)
			trackingQueue = null;
		trackingQueueLock.unlock();
	}
	
	private boolean processHTTPpost(HttpClient httpClient, String url_select, String jsonRequest) {
		InputStream inputStream = null;
		
		HttpPost httpPost = new HttpPost(url_select);

		
		StringEntity se = null;
		try {
			se = new StringEntity(jsonRequest);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		httpPost.setEntity(se);
		//sets a request header so the page receving the request
		//will know what to do with it
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		// Read content & Log

		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			inputStream = httpEntity.getContent();
		} catch (IOException e) {
//			e.printStackTrace();
			// RESPONSE FAILED. tested from wifi failure
			return false;
		}


		// Convert response to string using String Builder
		
		try {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder sBuilder = new StringBuilder();

			String line = null;
			while ((line = bReader.readLine()) != null)
				sBuilder.append(line + "\n");

			inputStream.close();
			
			resultString = sBuilder.toString();
			this.processResponse(resultString);

		} catch (Exception e) {
			Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
		}
		
		return true;

	}
	
	private void processResponse(String response) throws JSONException{
		JSONObject jsonResponse = new JSONObject(response);
		if(DopamineBase.debugMode) System.out.println("\nResulting JSON:" + jsonResponse.toString(2));
		
		if(jsonResponse.has("reinforcementFunction"))
			resultFunction = jsonResponse.getString("reinforcementFunction");
		else
			resultFunction = "";
		
		if(jsonResponse.has("reinforcementArguments")){
			String array = jsonResponse.getString("reinforcementArguments");
			arguments = new JSONArray(array);
		}
		else
			arguments = new JSONArray();
		
		if(jsonResponse.has("error")){
			JSONArray array = new JSONArray(jsonResponse.getString("error"));
			if(array.length() > 0)
				error = array.toString();
		}
		else
			error = "";
		
		if(jsonResponse.has("status"))
			status = jsonResponse.getString("status");
		else
			status = "";
		
		return;
	}


    @Override
    protected void onPostExecute(String o) {
    	super.onPostExecute(o);
    }


}