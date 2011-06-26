package com.eam.android.utils;

import java.security.PublicKey;

public class Config {

	//public static final String TARGET_DOMAIN = "10.0.2.2";	//localhost
    //sample mwa:  https://dg.enigmaserver.com/mwa/HomePage/Home.aspx
    //sample REST:  https://dg.enigmaserver.com/ws/eam.svc


	//TODO: JIA:  Change this back to the correct way
	public static String TARGET_DOMAIN  = "eam.net-pac.com";
	public static final Integer SERVICE_PORT = 443;		//for for SSL
	public static String token;
	public static String username;
	public static final String FILE_NAME = "config.txt";
	public static final String URL_CHANGE_MSG = "You have accessed an option that should only be used by an Enigma System Admin.  If you continue, you will need to configure a new URL or the app will no longer function.";
	public static final int KILL_APP = 123;
	public static final int CHANGE_URL = 234;
	public static final int RETURN_TO_LOGIN = 345;
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int SOCKET_TIMEOUT = 8000;
	
	
	public static String getHomePageURL() {
		return "https://" + TARGET_DOMAIN + "/mwa/HomePage/Home.aspx";
	}

	public static String getWidgetLoginServicePostUrl() {
		//return "http://" + TARGET_DOMAIN + ":" + SERVICE_PORT + "/ws/eam.svc/rest/widgetlogin";
        return "https://" + TARGET_DOMAIN + "/ws/eam.svc/rest/widgetlogin";
	}

	public static String getRegularLoginServicePostUrl() {
		return "http://" + TARGET_DOMAIN + "/ws/eam.svc/rest/login";
	}
	
	/**
	 * This is just a bogus thing we're putting here because we want to try the httpS version if the regular
	 * http version doesn't work.  Definitly need to change for better support when this thing goes live
	 * @return
	 */
	public static String getSecureLoginURLforConnectionTest() {
		return "https://" + TARGET_DOMAIN + "/ws/eam.svc/rest/login";
	}
	
	public static String getWidgetLoginUrl() {
		return "https://" + TARGET_DOMAIN + "/mwa/WidgetLogin.aspx";
	}

	public static String getNewEamUrl() {
		return "https://" + TARGET_DOMAIN + "/mwa/ViewEam/EditEam.aspx?EamID=0";
	}
	
	public static String getHelpUrl() {
		return "https://" + TARGET_DOMAIN + "/mwa/Help/Help.aspx";
	}
	
	public static String getDashboardUrl() {
		return "https://" + TARGET_DOMAIN + "/mwa/Dashboard/Dashboard.aspx";
	}
	
	public static String getOptionsUrl() {
		return "https://" + TARGET_DOMAIN + "/mwa/Options/Options.aspx";
	}
	
	
	
	//sample useful test information
	///EamMobileWebApp/Help/Help.aspx
	//http://eam.net-pac.com/EamMobileWebApp/ViewEam/EditEam.aspx?EamID=0
	//http://eamws-qa.net-pac.com:88/EamWebService/Eam.svc/Rest/help
}
