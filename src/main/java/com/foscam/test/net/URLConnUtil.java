package com.foscam.test.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

/**
 * 用于打开连接和解析从百度云返回数据
 * @author hujun
 */
public class URLConnUtil {
	
	private static SSLSocketFactory sslFactory = null;
	
	static {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
		        @Override
		        public void checkClientTrusted(X509Certificate[] chain, String s) {
		        }

		        @Override
		        public void checkServerTrusted(X509Certificate[] chain, String s) {
		        }

		        @Override
		        public X509Certificate[] getAcceptedIssuers() {
		        	return null;
		        }
		    }}, new SecureRandom());
			
			sslFactory = sslContext.getSocketFactory();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 以POST方式请求
	 * @param address
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection getURLConnnection(String address,String param) throws IOException{
		HttpURLConnection conn = null;
		URL url = new URL(address);
		conn = (HttpURLConnection)url.openConnection();
		if (address.startsWith("https")) {
        	((HttpsURLConnection)conn).setSSLSocketFactory(sslFactory);
        	((HttpsURLConnection)conn).setHostnameVerifier(new CustomizedHostnameVerifier());
        }
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());     
        out.write(param); //向页面传递数据。post的关键所在！     
        // remember to clean up     
        out.flush();     
        out.close();
        
        return conn;
	}
	/**
	 * 以GET方式请求
	 * @param address
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection getURLConnnection(String uri) throws IOException{
		HttpURLConnection conn = null;
		URL url = new URL(uri);
		conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
		conn.setDoInput(true);
        return conn;
	}
	/**
	 * 
	 * @param conn
	 * @return String
	 * @throws IOException 
	 * @throws IOException
	 */
	public static String getStringByURLConnection(HttpURLConnection conn) throws IOException  {
		
		if(conn==null) return "";
		String sTotalString  = ""; 
		String sCurrentLine  = "";    
        InputStream l_urlStream = conn.getInputStream();
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
                l_urlStream));     
        while ((sCurrentLine = l_reader.readLine()) != null) {     
            sTotalString += sCurrentLine;     
        }     
        l_urlStream.close();
		
		return sTotalString;
	}
	/**
	 * 
	 * @param InputStream l_urlStream 
	 * @return 返回一个jsonObject对象
	 * @throws IOException
	 */
	public static JSONObject getJsonByURLConnection(HttpURLConnection conn) throws IOException{
		
		return JSONObject.fromObject(getStringByURLConnection(conn));
	}
	
	public static String getStringByInputStream(InputStream l_urlStream) throws IOException{
		
		if(l_urlStream==null) return "";
		String sTotalString  = ""; 
		String sCurrentLine  = "";    
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
                l_urlStream));     
        while ((sCurrentLine = l_reader.readLine()) != null) {     
            sTotalString += sCurrentLine;     
        }     
        l_urlStream.close();
		
		return sTotalString;
	}
	/**
	 * 
	 * @param InputStream l_urlStream 
	 * @return 返回一个jsonObject对象
	 * @throws IOException
	 */
	public static JSONObject getJsonByInputStream(InputStream l_urlStream) throws IOException{
		
		return JSONObject.fromObject(getStringByInputStream(l_urlStream));
	}

	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}
