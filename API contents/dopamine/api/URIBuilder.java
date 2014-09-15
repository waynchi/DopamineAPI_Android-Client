package com.dopamine.api;

/**
 * Created by pradeepbk4u on 6/23/14.
 */
public class URIBuilder {

    private URIBuilder() {
    }
    
    // For Server dev
//  private static final String uriBase = "https://staging.usedopamine.com/v2/app/";
    private static final String uriBase = "https://api.usedopamine.com/v2/app/";

    
    private static StringBuffer baseURI(){
    	StringBuffer sb = new StringBuffer(uriBase);
    	sb.append(DopamineBase.appID);
    	return sb;
    }
    
    protected static String getInitilizationURI(){
    	StringBuffer sb = baseURI();
    	sb.append("/init/");
    	return sb.toString();
    }
    
    protected static String getTrackingURI(){
    	StringBuffer sb = baseURI();
    	sb.append("/track/");
    	return sb.toString();
    }
    
    protected static String getReinforcementURI(){
    	StringBuffer sb = baseURI();
    	sb.append("/reinforce/");
    	return sb.toString();
    }
    
}
