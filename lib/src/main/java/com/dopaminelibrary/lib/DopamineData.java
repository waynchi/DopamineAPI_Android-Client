package com.dopaminelibrary.lib;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by pradeepbk4u on 4/18/14.
 */
public class DopamineData {


    public DopamineData() {
        identityObject = new Identity();
    }

    public static enum IdType {
        MAC,
        Email
    }


    private Identity identityObject;
    Pair<String, String> identity;
    private String key;
    private String token;
    private int idType;
    private String uniqueID;
    private String eventID;
    private MetaData data;
    private ArrayList<String> persistentMetaData;
    ArrayList<String> feedbackFunctions;
    private String build;

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    private String appID;

    public String getVersionID() {
        return versionID;
    }

    private String versionID;


    public ArrayList<String> getPersistentMetaData() {

        return persistentMetaData;
    }

    public void setPersistentMetaData(ArrayList<String> persistentMetaData) {
        this.persistentMetaData = persistentMetaData;
    }

    public String getAppID() {

        return appID;
    }

    public void setIdentity(String key, String value) {

        this.identity = new Pair<String, String>(key, value);
    }

    public void setAppID(String appID) {

        this.appID = appID;
    }


    public void setBuild(String build) {

        this.build = build;
    }


    public ArrayList<String> getRewardFunctions() {

        return rewardFunctions;
    }

    public void setRewardFunctions(ArrayList<String> rewardFunctions) {




        this.rewardFunctions = rewardFunctions;
    }

    ArrayList<String> rewardFunctions;

    public ArrayList<String> getFeedbackFunctions() {

        return feedbackFunctions;
    }

    public void setFeedbackFunctions(ArrayList<String> feedbackFunctions) {
        this.feedbackFunctions = feedbackFunctions;
    }

    public String getKey() {
        return key;
    }

    public String getToken() {
        return token;
    }

    public int getIdType() {
        return idType;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getEventID() {
        return eventID;
    }

    public MetaData getData() {
        return data;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setToken(String value) {
        this.token = value;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setData(MetaData data) {
        this.data = data;
    }


    public String getBuild() {

        return sha1(getBuildString());
    }


    private String getBuildString() {
        StringBuilder builder = new StringBuilder();
        if (rewardFunctions != null && feedbackFunctions != null) {
            for (String reward : rewardFunctions) {
                builder.append(reward);
            }


            for (String feedback : feedbackFunctions) {
                builder.append(feedback);
            }

        }

        return builder.toString();
    }



    public String sha1(String s) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }


    public void setIdentityEmail(String email) {
        identityObject.addEmail(email);
    }

    public void setIdentityMacAddress(String mac) {
        identityObject.setMac(mac);
    }


    public String getIdentity() {
        return identityObject.getIdentity();
    }


    class Identity {


        public Identity() {
            email = new ArrayList<String>();

        }

        private ArrayList<String> email;
        private String mac;


        private ArrayList<String> getEmail() {
            return email;
        }

        public void addEmail(String email) {
            this.email.add(email);
        }

        private String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }


        public String getIdentity() {
            JSONArray object = new JSONArray();

            try {


                if (mac != null) {
                    JSONObject macObject = new JSONObject();
                    macObject.put("_MAC", mac);

                    object.put(macObject);
                }


                JSONObject emailObject = new JSONObject();
                JSONArray emailArray = new JSONArray();
                if (email != null && email.size() > 0) {

                    for (int i = 0; i < email.size(); i++) {
                        emailArray.put(email.get(i));
                    }
                    emailObject.put("_EMAIL", emailArray);


                    object.put(emailObject);


                }
            } catch (Exception e) {

            }
            return object.toString();

        }


    }

}
