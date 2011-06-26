package com.eam.android.utils;

import com.eam.android.EamHome;

import android.app.Activity;
import android.webkit.WebView;

public class MenuHandler {
    
    public static void goToHome(Activity source, String userName) {
    	EamHome activity = (EamHome) source;
    	WebView browser = activity.getWebView();
    	browser.loadUrl(Config.getHomePageURL());
    }

    public static void goToNewEam(Activity source) {
    	EamHome activity = (EamHome) source;
    	WebView browser = activity.getWebView();
    	browser.loadUrl(Config.getNewEamUrl());
    }
    
    public static void goToHelp(Activity source) {
    	EamHome activity = (EamHome) source;
    	WebView browser = activity.getWebView();
    	browser.loadUrl(Config.getHelpUrl());
    }
    
    public static void goToDashboard(Activity source) {
    	EamHome activity = (EamHome) source;
    	WebView browser = activity.getWebView();
    	browser.loadUrl(Config.getDashboardUrl());    	
    }

    public static void goToOptions(Activity source) {
    	EamHome activity = (EamHome) source;
    	WebView browser = activity.getWebView();
    	browser.loadUrl(Config.getOptionsUrl());    	
    }
}
