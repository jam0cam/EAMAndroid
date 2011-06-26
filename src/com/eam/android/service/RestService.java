package com.eam.android.service;

import java.io.IOException;

import android.net.wifi.WifiConfiguration;
import org.apache.commons.ssl.HttpSecureProtocol;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.eam.android.exceptions.ServerException;

import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class RestService {
	
	String targetDomain;		//"10.0.2.2";
	String xmlContentToSend;
	Integer port;	//55672
	
	public RestService(String targetDomain, String XmlContent, Integer port) {
		this.targetDomain = targetDomain;
		this.xmlContentToSend = XmlContent;
		this.port = port;
	}
	
	public String testConnectivity() {
		return executeGet("http://10.0.2.2:55672/LoginService/Get");
	}
	
	public String executeGet(String GetUrl) {	//"http://10.0.2.2:55672/LoginService/Get"

		HttpGet get = new HttpGet(GetUrl);
		
	    String result=null;
	    HttpEntity entity = null;
	    HttpClient client = new DefaultHttpClient();
	    try {
	        HttpResponse response=client.execute(get);
	        entity = response.getEntity();
	        result = EntityUtils.toString(entity);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    if (entity!=null)
	    try {
	        entity.consumeContent();
	    } catch (IOException e) {}
	    }
	    return result;
	}
	
	
	public String executePost(String postUrl) throws Exception{		//"http://10.0.2.2:55672/LoginService/"

		//DefaultHttpClient httpClient = new DefaultHttpClient();
DefaultHttpClient httpClient;

SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

HttpParams params = new BasicHttpParams();
params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

        httpClient = new DefaultHttpClient(cm, params);

		HttpHost targetHost = new HttpHost(targetDomain, port, "https");

		// Using POST here
		HttpPost httpPost = new HttpPost(postUrl);
		// Make sure the server knows what kind of a response we will accept
		httpPost.addHeader("Accept", "text/xml");
		// Also be sure to tell the server what kind of content we are sending
		httpPost.addHeader("Content-Type", "application/xml");
		       
		try
		{
		    StringEntity entity = new StringEntity(xmlContentToSend, "UTF-8");
		    entity.setContentType("application/xml");
		    httpPost.setEntity(entity);
		   
		        // execute is a blocking call, it's best to call this code in a thread separate from the ui's
		    HttpResponse response = httpClient.execute(targetHost, httpPost);
		    HttpEntity ent = response.getEntity();
		    String result = EntityUtils.toString(ent);
		    return result;
		        // Have your way with the response
		}
		catch (Exception ex)
		{
		        ex.printStackTrace();
		        throw new ServerException(ex.getMessage());
		}		
	}
}
