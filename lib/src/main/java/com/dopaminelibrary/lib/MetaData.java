package com.dopaminelibrary.lib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pradeepbk4u on 4/18/14.
 */
public class MetaData {

    private Map<String, String> data;
    private JSONObject jsonData;


    public MetaData() {
        data = new HashMap<String, String>();
        jsonData = new JSONObject();
    }

    public void addData(String key, String value) {

        data.put(key, value);
        try {
            jsonData.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public Map getData() {
        return data;
    }

    public String getJsonData() {
        return jsonData.toString();
    }


}
