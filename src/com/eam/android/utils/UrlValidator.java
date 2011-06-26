package com.eam.android.utils;

import com.eam.android.dao.LoginDao;
import com.eam.android.exceptions.ServerException;
import com.eam.android.model.LoginItem;

public class UrlValidator
{
	public static boolean isValidEamURL(String url) {
		
		Config.TARGET_DOMAIN = url;		
		LoginItem item = new LoginItem();
		LoginDao dao = new LoginDao();
		try {
			return dao.containsValidLoginData(item);
		} catch (ServerException e) {
			return false;
		}
	}
}