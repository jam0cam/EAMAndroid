package com.eam.android.utils;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.eam.android.model.LoginItem;

public class LoginXmlSerializer extends DefaultHandler implements
		IXmlSerializer {

	StringBuffer buff;
	boolean buffering;
	LoginItem loginItem;

	@Override
	public String objectToXml(Object object) {
		LoginItem item = (LoginItem)object;
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<LoginItem xmlns=\"http://schemas.datacontract.org/2004/07/EamWcfServiceLibrary\">");
		sb.append("<Password>" + item.getPassword() + "</Password>");
		sb.append("<UserName>" + item.getUserName() + "</UserName>");
		sb.append("<Longitude>" + item.getLongitude() + "</Longitude>");
		sb.append("<Latitude>" + item.getLatitude() + "</Latitude>");
		sb.append("</LoginItem>");

		return sb.toString();
	}

	@Override
	public Object xmlToObject(String xml) throws Exception {
		/* Get a SAXParser from the SAXPArserFactory. */
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();

		/* Get the XMLReader of the SAXParser we created. */
		XMLReader xr = sp.getXMLReader();
		/* Create a new ContentHandler and apply it to the XML-Reader */
		xr.setContentHandler(this);
		/* Parse the xml-data from our URL. */

		StringReader sr = new StringReader(xml);
		InputSource is = new InputSource(sr);
		xr.parse(is);

		return loginItem;
	}

	@Override
	public void startDocument() throws SAXException {
		loginItem = new LoginItem();
		buff = new StringBuffer();
	}

	@Override
	public void characters(char ch[], int start, int length) {

		buff.append(ch, start, length);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		String content = buff.toString();
		buff = new StringBuffer();

		if (qName.equalsIgnoreCase("password")) {
			loginItem.setPassword(content);
		} else if (qName.equalsIgnoreCase("username")) {
			loginItem.setUserName(content);
		}
	}

}
