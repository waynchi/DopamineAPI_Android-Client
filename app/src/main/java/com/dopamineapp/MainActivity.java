package com.dopamineapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.dopaminelibrary.lib.Dopamine;
import com.dopaminelibrary.lib.DopamineData;
import com.dopaminelibrary.lib.MetaData;
import com.dopaminelibrary.lib.OnTaskCompleted;

import java.util.ArrayList;

/**
 * Created by pradeepbk4u on 4/18/14.
 */

public class MainActivity extends ActionBarActivity implements OnTaskCompleted {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DopamineData data = new DopamineData();


        data.setIdentityEmail("ramsay123456789@useDopamine.com");
        data.setIdentityMacAddress("AB:CD:EF:GH:IJ");
        data.setAppID("5353459b347e852a2f9b8212");
        data.setToken("50074929278343925353459b347e852a2f9b8212");

        data.setKey("6ce9013c86d99f3be3b17514a6a51c169c1e6793");
        data.setVersionID("version1");

        ArrayList<String> rewardFunctions = new ArrayList<String>();
        rewardFunctions.add("rewardFunction1");
        rewardFunctions.add("rewardFunction2");

        ArrayList<String> feedBackFunction = new ArrayList<String>();
        feedBackFunction.add("feedBackFunction1");
        data.setRewardFunctions(rewardFunctions);
        data.setFeedbackFunctions(feedBackFunction);
        data.setEventID("eventID1");
        MetaData dopData = new MetaData();
        dopData.addData("heartRate", "120");
        dopData.addData("position", "35.4433431, 123.52342342");
        dopData.addData("some array","[1,2,3,4]");
        data.setData(dopData);

        Dopamine.InitDopamine(data, this);
        Dopamine.reEnforce();


    }



    public void makeRequest(View view) {
       Dopamine.reEnforce();
    }


    @Override
    public void onTaskCompleted(String s) {

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(s);
    }




}
