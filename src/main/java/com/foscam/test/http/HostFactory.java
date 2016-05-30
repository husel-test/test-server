package com.foscam.test.http;

import java.util.HashMap;

public class HostFactory {
	
	public final static String encodingCharSet = "UTF-8";
	
	private static HashMap<String, HostAndPort> map  = new HashMap<String, HostAndPort>();
	
	public static HostAndPort getHost(String targetUrl){
		
		if(targetUrl == null || targetUrl.isEmpty()) return null;
		
		HostAndPort result = map.get(targetUrl);
		
		if(result == null){
			String host = targetUrl;
			int port = 80;
			String[] arr = targetUrl.split(":");
			if (arr.length == 3) {
				host = arr[0] + ":" + arr[1];
				port = Integer.parseInt(arr[2].split("/")[0]);
			}
			synchronized (HostFactory.class) {
				HostAndPort hp = new HostAndPort(host,port);
				result = hp;
				map.put(targetUrl, hp);
			}
		}	
		
		return result;
	}

}
class HostAndPort{
	
	private String host;
	private int port;
	
	public HostAndPort(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return host+" , "+port;
	}
	
}