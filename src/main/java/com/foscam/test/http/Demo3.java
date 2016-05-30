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
 * bulider不变，
 * 每次发送用同一个client，
 * 发送完毕不关闭client
 * 关闭response
 * 结果：平均大约4ms,和每次创建client差不多
 * @author Administrator
 *
 */
public class Demo3 {
	
	private static HttpClientBuilder bulider = HttpClientBuilderFactory.getHttpClientBuilder("http://172.16.40.37", 80);
	
	public static void main(String[] args) {
		
		
		CloseableHttpClient client =  bulider.build();;
		
		for (int i = 1; i < 100; i++) {
			
			MessageBody body = new MessageBody();
			body.setId(i);
			body.setSenderTag("aabbccddeeff");
			body.setUserTag("test.tag.1010131geg");
			body.setMsg("test-context...."+i);
			body.setMsgTime("2016.2.19");
			
			t1(client,body);
			
		}
			
	}
	public static void t1(CloseableHttpClient client, MessageBody body){
		String serverUrl = "http://172.16.40.37:80";
		long t1 = System.currentTimeMillis();
		
		CloseableHttpResponse response = null;
		try {
			
			
			
			HttpPost post = new HttpPost(serverUrl);
			List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			formparams.add(new BasicNameValuePair("id", String.valueOf(body.getId())));
			formparams.add(new BasicNameValuePair("senderTag", body.getSenderTag()));
			formparams.add(new BasicNameValuePair("userTag", body.getUserTag()));
			formparams.add(new BasicNameValuePair("msgTime", body.getMsgTime()));
			formparams.add(new BasicNameValuePair("msg", body.getMsg()));
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
			
			//如果关闭client，那么会关闭整个连接池，无需关闭client
			//HttpClientUtils.closeQuietly(client);
			
			System.out.println(System.currentTimeMillis() - t1);
		}
	}

}
