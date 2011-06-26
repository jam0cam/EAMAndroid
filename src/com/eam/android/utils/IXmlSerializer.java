package com.eam.android.utils;

public interface IXmlSerializer {

	Object xmlToObject(String xml)  throws Exception ;
	String objectToXml(Object object)  throws Exception ;
}
