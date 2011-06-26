package com.eam.android.dao;

import com.eam.android.exceptions.ServerException;
import com.eam.android.model.LoginItem;
import com.eam.android.model.ServerResponse;
import com.eam.android.service.RestService;
import com.eam.android.utils.Config;
import com.eam.android.utils.LoginXmlSerializer;
import com.eam.android.utils.ServerResponseXMLSerializer;

public class LoginDao {
	
	/**
	 * Returns a token upon successful login.
	 * @param loginItem
	 * @return
	 * @throws ServerException
	 */
	public ServerResponse submitLogin(LoginItem loginItem) throws ServerException {
		LoginXmlSerializer serializer = new LoginXmlSerializer();
		
		String xmlData = serializer.objectToXml(loginItem);
		RestService service = new RestService(Config.TARGET_DOMAIN, xmlData, Config.SERVICE_PORT);		
		
		String response = null;
		
		try{
			String postUrl = Config.getSecureLoginURLforConnectionTest();
			response = service.executePost(postUrl);
		} catch (Exception e){
			throw new ServerException("Server error\n" + e.getMessage());
		}
		
		ServerResponseXMLSerializer responseSerializer = new ServerResponseXMLSerializer();
		
		ServerResponse serverResponse = new ServerResponse();
		serverResponse.token = responseSerializer.getToken(response); 

		return serverResponse;
	}
	
	public boolean containsValidLoginData(LoginItem item) throws ServerException {
		LoginXmlSerializer serializer = new LoginXmlSerializer();
		
		String xmlData = serializer.objectToXml(item);
		RestService service = new RestService(Config.TARGET_DOMAIN, xmlData, Config.SERVICE_PORT);		
		
		String firstPostUrl = Config.getRegularLoginServicePostUrl();
		String secondPostUrl = Config.getSecureLoginURLforConnectionTest();
		
		if (!tryUrl(service, firstPostUrl)) {	//if the first URL does not work, try the second one
			return tryUrl(service, secondPostUrl);
		}
		
		
		return true;
	}
	
	private boolean tryUrl(RestService service, String postUrl) {
		String response = null;
		
		try{			
			response = service.executePost(postUrl);
		} catch (Exception e){
			return false;
		}

		if (response.contains("<ErrorMessage>Invalid username or password</ErrorMessage>"))
			return true;
		
		return false;
		
	}
}
