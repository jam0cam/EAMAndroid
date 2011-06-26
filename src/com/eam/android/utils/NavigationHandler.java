package com.eam.android.utils;

import android.app.Activity;

/**
 * This is a class that handles the navigation of activities.
 * @author jitse
 *
 */
public class NavigationHandler {
	
	public static void goToLogin(Activity activity) {
		activity.setResult(Config.RETURN_TO_LOGIN);
		activity.finish();
	}
	
	public static void exitApp(Activity activity) {
		activity.setResult(Config.KILL_APP);
		activity.finish();
	}
	
	public static void changeUrl(Activity activity) {
		activity.setResult(Config.CHANGE_URL);
		activity.finish();
	}
}
