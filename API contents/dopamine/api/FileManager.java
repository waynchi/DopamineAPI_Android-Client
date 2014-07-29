package com.dopamine.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

public class FileManager {
	private static Context context = DopamineBase.context;

	public static String trackingFilename = "trackingQueue.json";
	private static final String TRACKINGJSONREQUEST_string = "TrackingJSONRequest";

	static String getFileValue(String fileName) {

		try {
			StringBuffer outStringBuf = new StringBuffer();
			String inputLine = "";
			FileInputStream fIn = context.openFileInput(fileName);
			InputStreamReader isr = new InputStreamReader(fIn);
			BufferedReader inBuff = new BufferedReader(isr);
			while ((inputLine = inBuff.readLine()) != null) {
				outStringBuf.append(inputLine);
				outStringBuf.append("\n");
			}
			inBuff.close();
			return outStringBuf.toString();
		} catch (IOException e) {
			return null;
		}
	}

	static boolean appendFileValue(String fileName, String value) {
		return writeToPrivateFile(fileName, value, Context.MODE_APPEND);
	}

	static boolean setFileValue(String fileName, String value, Context context) {
		return writeToPrivateFile(fileName, value, Context.MODE_PRIVATE);
	}

	static boolean writeToPrivateFile(String fileName, String value, int writeOrAppendMode) {
		try {
			FileOutputStream fOut = context.openFileOutput(fileName, writeOrAppendMode);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			osw.write(value);
			osw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	static boolean writeToPublicFile(String absoluteFileName, String value) {
		try {
			File file = new File(absoluteFileName);
			FileOutputStream foStream = new FileOutputStream(file);
			OutputStreamWriter osWriter = new OutputStreamWriter(foStream);
			osWriter.write(value);
			osWriter.close();
		} catch (Exception e) {
			return false;
		}

		return true;

	}

	static void deleteFile(String fileName) {
		context.deleteFile(fileName);
	}
	
	static void overwriteTrackingRequestLog(Queue<String> queue) {
		try {
			deleteFile(trackingFilename);

			FileOutputStream fOut = context.openFileOutput(trackingFilename, Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);

			JsonWriter writer = new JsonWriter(osw);
			writer.setIndent("  ");

			writer.beginArray();
			for (String trackingRequest : queue) {
				// JSON Tracking Object
				writer.beginObject();
				
				writer.name(TRACKINGJSONREQUEST_string).value(trackingRequest);
				
				writer.endObject();
			}
			writer.endArray();

			writer.close();
			
			if(DopamineBase.debugMode) System.out.println("\tTracking File:\n" + getFileValue(trackingFilename));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static Queue<String> loggedTrackingRequestsTOqueue() {
		Queue<String> queue = new LinkedList<String>();
		
		FileInputStream fIn;
		try {
			fIn = context.openFileInput(trackingFilename);
		} catch (FileNotFoundException e1) {
			return queue;
		}

		InputStreamReader isr = new InputStreamReader(fIn);

		try {
			JsonReader reader = new JsonReader(isr);
			
			reader.beginArray();
			while (reader.hasNext()) {
				reader.beginObject();
				
				String fieldName = reader.nextName();
				if(fieldName.equals(TRACKINGJSONREQUEST_string)){
					String trackingRequest = reader.nextString();
					queue.add(trackingRequest);
				}
				
				reader.endObject();
			}
			reader.endArray();

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return queue;
		}

		return queue;
	}

}