package com.foscam.test.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
/**
 * 测试发送单个post请求的正确性
 * @author Administrator
 *
 */
public class Demo {
	
	
	public static void main(String[] args) {
		
		String serverUrl = "http://172.16.40.37:80";
		HostAndPort host = HostFactory.getHost(serverUrl);
		
		long t1 = System.currentTimeMillis();
		
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		try {
			HttpClientBuilder bulider = HttpClientBuilderFactory.getHttpClientBuilder(host.getHost(), host.getPort());
			
			client = bulider.build();
			
			HttpPost post = new HttpPost(serverUrl);
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("id", "11"));
			formparams.add(new BasicNameValuePair("senderTag", "aabbccddeeff"));
			formparams.add(new BasicNameValuePair("userTag", "test.tag.13190313"));
			formparams.add(new BasicNameValuePair("msgTime", "2015-02-19"));
			formparams.add(new BasicNameValuePair("msg", "testaaaa"));
			UrlEncodedFormEntity params = new UrlEncodedFormEntity(formparams, HostFactory.encodingCharSet);
			post.setEntity(params);
			
			response = client.execute(post);
			HttpEntity resEntity = response.getEntity();
			
			//返回结果
			String returnValue = EntityUtils.toString(resEntity, HostFactory.encodingCharSet);
			System.out.println(returnValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(client);
			System.out.println(System.currentTimeMillis() - t1);
		}
			
	}
	

}
