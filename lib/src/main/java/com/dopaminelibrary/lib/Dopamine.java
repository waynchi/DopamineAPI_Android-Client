package com.dopaminelibrary.lib;

import android.os.AsyncTask;
import android.util.Log;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by pradeepbk4u on 4/18/14.
 */
public class Dopamine {

    private InputStream inputStream;

    private String result;

    private static Dopamine dopamine;
    private static DopamineData data;
    OnTaskCompleted listener;



    public static void InitDopamine(DopamineData dopamineData, OnTaskCompleted listner) {
        dopamine = new Dopamine(dopamineData, listner);
        registerService();

    }
    private Dopamine(DopamineData dopamineData, OnTaskCompleted listener) {

        data = dopamineData;
        this.listener = listener;
    }


    private Dopamine() {
    }


    public static void registerService() {
        getInitRequest();
    }


    public void callReforce() {
        URIBuilder uri = new URIBuilder(data.getAppID());
        new DopamineRequest().execute(getInitRequest(), uri.getURI(URIBuilder.URI.INIT));
    }

    public static void reEnforce() {
        dopamine.callReforce();
    }


    public static void feedBack() {


    }


    private class DopamineRequest extends AsyncTask<String, Void, String> {


        //  private OnTaskCompleted listener;

        public DopamineRequest() {
            // this.listener=listener;
        }






        @Override
        protected String doInBackground(String... params) {

            SSLContext ctx = null;
            try {
                 ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[] { new CustomX509TrustManager() },
                        new SecureRandom());

                String url_select = params[1];
                HttpClient httpClient = new DefaultHttpClient();


                SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
                ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                ClientConnectionManager ccm = httpClient.getConnectionManager();
                SchemeRegistry sr = ccm.getSchemeRegistry();
                sr.register(new Scheme("https", ssf, 443));



                HttpPost httpPost = new HttpPost(url_select);





            StringEntity se = null;
            try {
                se = new StringEntity(params[0]);
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
                e.printStackTrace();
            }


            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuilding & BufferedReader", "Error converting result " + e.toString());
            }


            } catch (Exception e) {

            }


            return result;
        }


        @Override
        protected void onPostExecute(String o) {
            //your stuff
            listener.onTaskCompleted(o);
        }


    }


    private static String getInitRequest() {
        JSONObject jsonObject = getBaseRequest();

        if (jsonObject != null) {
            try {
                jsonObject.put("eventID", data.getEventID());
                jsonObject.put("build", data.getBuild());
				
				// <akash>
//                StringBuilder rewardString = new StringBuilder();
//                rewardString.append("[");
//                for(String reward :  data.getRewardFunctions()) {
//                	jsonObject.put("rewardFunctions", reward);
//                }
//                rewardString.append("]");
//                System.out.println(rewardString.toString());
//
//                jsonObject.put("rewardFunctions" , rewardString.toString());
                
                JSONArray rewardArray = new JSONArray();
                for(String reward : data.getRewardFunctions()){
                	rewardArray.put(reward);
                }
                jsonObject.put("rewardFunctions", rewardArray);

//                StringBuilder feedbackString = new StringBuilder();
//                feedbackString.append("[");
//                for(String feedback :  data.getFeedbackFunctions()) {
//                    feedbackString.append(feedback);
//                }
//                feedbackString.append("]");
//                System.out.println(feedbackString.toString());
//                jsonObject.put("feedbackFunctions" , feedbackString.toString());
                JSONArray feedbackArray = new JSONArray();
                for(String feedback : data.getFeedbackFunctions())
                	feedbackArray.put(feedback);
                jsonObject.put("feedbackFunctions", feedbackArray);
                // </akash>
				
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(data.getData().getJsonData());
                jsonObject.put("metaData", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
                return "0";
            }
        } else {
            //Errord
        }


        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }


    private static String getTrackRequest() {


        JSONObject jsonObject = getBaseRequest();

        if (jsonObject != null) {
            try {
                jsonObject.put("build", data.getBuild());

                jsonObject.put("eventName", data.getEventID());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(data.getData().getJsonData());
                jsonObject.put("metaData", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
                return "0";
            }
        } else {
            //Errord
        }

        return jsonObject.toString();


    }


    private static String getRewardRequest() {


        JSONObject jsonObject = getBaseRequest();

        if (jsonObject != null) {
            try {
                jsonObject.put("build", data.getBuild());

                jsonObject.put("eventName", data.getEventID());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(data.getData().getJsonData());
                jsonObject.put("metaData", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
                return "0";
            }
        } else {
            //Errord
        }


       ;
        return jsonObject.toString();


    }


    private static JSONObject getBaseRequest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", data.getKey());
            jsonObject.put("token", data.getToken());
            jsonObject.put("versionID", data.getVersionID());
            jsonObject.put("identity", data.getIdentity());


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }


}
