package com.eam.android.presenter;

import com.eam.android.dao.LoginDao;
import com.eam.android.exceptions.ServerException;
import com.eam.android.model.LoginItem;
import com.eam.android.model.ServerResponse;

import android.app.Activity;

public class LoginPresenter {

	Activity view;
	LoginDao loginDao;
	
	public LoginPresenter(Activity view) {
		this.view = view;	//this will have to change to be a concrete view of the login screen.
		loginDao = new LoginDao();
	}
	
	
	/**
	 * On Successful login, return token
	 * @param userName
	 * @param password
	 * @return
	 * @throws ServerException
	 */
	public ServerResponse submitLogin(String userName, String password, double lat, double lon) throws ServerException {	
		LoginItem item = new LoginItem();
		item.setUserName(userName);
		item.setPassword(password);		
		item.setLongitude(lon);
		item.setLatitude(lat);
		
		ServerResponse result = loginDao.submitLogin(item);
		
		return result;
	}
}
