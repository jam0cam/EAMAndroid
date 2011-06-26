package com.eam.android.utils;

import org.xml.sax.helpers.DefaultHandler;

import com.eam.android.model.ServerResponse;

public class ServerResponseXMLSerializer extends DefaultHandler implements IXmlSerializer {

	@Override
	public ServerResponse xmlToObject(String xml) {
		return null;
	}

	@Override
	public String objectToXml(Object object) throws Exception {
		return null;
	}
	
	public String getToken(String xml) {
		if (!xml.contains("<Token>"))
			return null;
		
		int startIdx = xml.indexOf("<Token>");
		int lastIdx = xml.indexOf("</Token>");
		String token = xml.substring(startIdx + 7, lastIdx);
		
		return token;
	}

}
