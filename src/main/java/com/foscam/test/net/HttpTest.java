package com.foscam.test.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTest {
	
	private static String charSet = "UTF-8";
	
	public static void main(String[] args) {
		
		testGet();
		
		testPost();
		testPost2();
		
	}
	public static void testGet() {
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClientBuilder.create().build();
			
			System.out.println(1);
			
			HttpGet getRequest = new HttpGet("http://172.16.40.11:40002/gateway?service=user.get_regist_country");
			
			response = client.execute(getRequest);
			
			HttpEntity entity = response.getEntity();
			String resultStr = EntityUtils.toString(entity, charSet);
			System.out.println(resultStr);
			HttpClientUtils.closeQuietly(response);
			System.out.println(2);	
			
			getRequest = new HttpGet("http://172.16.40.11:40002/gateway?service=user.get_offical_user_status&username=");
			response = client.execute(getRequest);
			System.out.println(EntityUtils.toString(response.getEntity(), charSet));
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.out.println(3);
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		
	}
	
	public static void testPost() {
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClientBuilder.create().build();
			
			HttpPost postRequest = new HttpPost("http://172.16.40.11:40002/gateway");
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("service", "user.activate_offical_user"));
			formparams.add(new BasicNameValuePair("data", "aaa"));
			UrlEncodedFormEntity params = new UrlEncodedFormEntity(formparams, charSet);
			postRequest.setEntity(params);
			response = client.execute(postRequest);
			
			HttpEntity entity = response.getEntity();
			
			String resultStr = EntityUtils.toString(entity, charSet);
			
			System.out.println(resultStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		
	}
	public static void testPost2() {
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClientBuilder.create().build();
			
			HttpPost postRequest = new HttpPost("http://172.16.40.11:40002/gateway?service=user.activate_offical_user&username=aabcc");
			
			response = client.execute(postRequest);
			HttpEntity entity = response.getEntity();
			String resultStr = EntityUtils.toString(entity, charSet);
			
			System.out.println(resultStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
		}
		
	}

}
