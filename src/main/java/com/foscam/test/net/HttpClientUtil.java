package com.foscam.test.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @author zhanglu
 */
public class HttpClientUtil {
	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	private static final String UTF8 = "UTF-8";	
	private static CloseableHttpClient httpClient = null;
	private static RequestConfig requestConfig = null;
	
	static {
		httpClient = HttpClients.custom().setRetryHandler(new HttpRequestRetryHandler() {
		    public boolean retryRequest(
		            IOException exception,
		            int executionCount,
		            HttpContext context) {
		        if (executionCount >= 5) {
		            // Do not retry if over max retry count
		            return false;
		        }
		        if (exception instanceof InterruptedIOException) {
		            // Timeout
		            return false;
		        }
		        if (exception instanceof UnknownHostException) {
		            // Unknown host
		            return false;
		        }
		        if (exception instanceof ConnectTimeoutException) {
		            // Connection refused
		            return false;
		        }
		        if (exception instanceof SSLException) {
		            // SSL handshake exception
		            return false;
		        }
		        HttpClientContext clientContext = HttpClientContext.adapt(context);
		        HttpRequest request = clientContext.getRequest();
		        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
		        if (idempotent) {
		            // Retry if the request is considered idempotent
		            return true;
		        }
		        return false;
		    }
		}).build();
		
		requestConfig = RequestConfig.custom()
		        .setSocketTimeout(10000)
		        .setConnectTimeout(10000)
		        .build();		
	}
	
	public static void closeResponse(HttpResponse resp) {
		if (resp!=null && resp.getEntity()!=null) {
			try {
				resp.getEntity().getContent().close();
			} catch (Exception e) {
				log.error( e.getMessage(), e);
			}
		}
	}
	public static void closeHttpClient() {
		try {
			httpClient.close();
		} catch (IOException e) {
			log.error( "httpClient.close error", e);
		}
	}
	public static String getForString(String url) throws HttpClientException {
		return getForString(url, null, null);
	}
	
	public static String getForString(HttpHost proxy, String url) throws HttpClientException {
		return getForString(proxy, url, null, null);
	}
	
	public static String getForString(String url, Map<String, String> params) throws HttpClientException {
		return getForString(url, params, null);
	}
	
	public static String getForString(HttpHost proxy, String url, Map<String, String> params) throws HttpClientException {
		return getForString(proxy, url, params, null);
	}
	
	public static String getForString(String url, Map<String, String> params, Map<String, String> headers) throws HttpClientException {				
		HttpResponse response = null;;
		String content = null;
		try {
			response = get(url, params, headers);
			content = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return content;
	}
	
	public static String getForString(HttpHost proxy, String url, Map<String, String> params, Map<String, String> headers)
			throws HttpClientException {				
		HttpResponse response = null;;
		String content = null;
		try {
			response = get(proxy, url, params, headers);
			content = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpClientException(e);
		} 
		return content;
	}
	
	public static HttpResponse get(HttpHost httpHost, String url, Map<String, String> params, Map<String, String> headers)
			throws IOException {
		HttpGet get = null;
		if (!CollectionUtils.isEmpty(params)) {
			get = new HttpGet(url + "?" + URLEncodedUtils.format(getNameValuePairsFromMap(params), UTF8));		
		} else {
			get = new HttpGet(url);
		}
		get.setConfig(requestConfig);
		if (headers!=null) {
			for (Entry<String, String> header : headers.entrySet()) {
				get.addHeader(header.getKey(), header.getValue());
			}
		}				
		HttpResponse response = httpHost==null ? httpClient.execute(get) : httpClient.execute(httpHost, get);
		if (response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK) {
			log.error( "StatusCode[%s],ReasonPhrase[%s]",response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
		}
		return response;
	}
	
	public static HttpResponse get(String url, Map<String, String> params, Map<String, String> headers)
			throws IOException {
		return get(null, url, params, headers);
	}
	
	public static String postUrlEncodedFormRequestForString(String url) throws HttpClientException {
		return postUrlEncodedFormRequestForString(url, null, null);
	}
	
	public static String postUrlEncodedFormRequestForString(String url, Map<String, String> params) throws HttpClientException {
		return postUrlEncodedFormRequestForString(url, params, null);
	}
	
	public static String postUrlEncodedFormRequestForString(String url, Map<String, String> params, Map<String, String> headers)
			throws HttpClientException {
		return postUrlEncodedFormRequestForString(null, url, params, headers);
	}
	
	public static String postUrlEncodedFormRequestForString(HttpHost proxy, String url, Map<String, String> params, Map<String, String> headers)
			throws HttpClientException {
		HttpResponse response = null;;
		String content = null;
		try {
			response = postUrlEncodedFormRequest(proxy, url, params, headers);
			content = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return content;
	}
	
	public static HttpResponse postUrlEncodedFormRequest(HttpHost httpHost, String url, Map<String, String> params, Map<String, String> headers) throws HttpClientException {
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		
		if (!CollectionUtils.isEmpty(params)) {
			try {
				post.setEntity(new UrlEncodedFormEntity(getNameValuePairsFromMap(params), UTF8));
			} catch (UnsupportedEncodingException e) {
				throw new HttpClientException(e);
			}
		}	
		if (headers!=null) {
			for (Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}
		}
		HttpResponse response;
		try {
			response = httpHost==null ? httpClient.execute(post) : httpClient.execute(httpHost, post);
		} catch (ClientProtocolException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return response;
	}
	
	public static String postStringBodyForString(String url, String text, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		HttpResponse response = null;;
		String content = null;
		try {
			response = postStringBody(url, text, contentType, headers);
			content = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return content;
	}
	
	public static HttpResponse postStringBody(String url, String text, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		return postStringBody(null, url, text, contentType, headers);
	}
	
	
	public static HttpResponse postStringBody(HttpHost httpHost, String url, String text, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		
		StringEntity entity = new StringEntity(text, contentType);
		if (text!=null) {
			post.setEntity(entity);
		}	
		if (headers!=null) {
			for (Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}
		}
		HttpResponse response;
		try {
			response = httpHost==null ? httpClient.execute(post) : httpClient.execute(httpHost, post);
		} catch (ClientProtocolException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return response;
	}
	
	public static String postByteArrayBodyForString(String url, byte[] bytes, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		HttpResponse response = null;;
		String content = null;
		try {
			response = postByteArrayBody(url, bytes, contentType, headers);
			content = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return content;
	}
	
	public static HttpResponse postByteArrayBody(String url, byte[] bytes, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		return postByteArrayBody(null, url, bytes, contentType, headers);
	}
	
	public static HttpResponse postByteArrayBody(HttpHost httpHost, String url, byte[] bytes, ContentType contentType, Map<String, String> headers) throws HttpClientException {
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);
		ByteArrayEntity entity = new ByteArrayEntity(bytes, contentType);
		
		if (bytes!=null && bytes.length>0) {
			post.setEntity(entity);
		}	
		if (headers!=null) {
			for (Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}
		}
		HttpResponse response;
		try {
			response = httpHost==null ? httpClient.execute(post) : httpClient.execute(httpHost, post);
		} catch (ClientProtocolException e) {
			throw new HttpClientException(e);
		} catch (IOException e) {
			throw new HttpClientException(e);
		}
		return response;
	}
	
	public static HttpResponse postUrlEncodedFormRequest(String url, Map<String, String> params, Map<String, String> headers) throws HttpClientException {
		return postUrlEncodedFormRequest(null,url, params, headers);
	}
	
	private static List<NameValuePair> getNameValuePairsFromMap(
			Map<String, String> params) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if (!CollectionUtils.isEmpty(params)) {
			for (Entry<String, String> e : params.entrySet()) {
				pairs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
			}
		}		
		return pairs;
	}
	
	public static class HttpClientException extends Exception {
		private static final long serialVersionUID = 1L;
		public HttpClientException() {
	        super();
	    }
	    public HttpClientException(String message) {
	        super(message);
	    }
	    public HttpClientException(String message, Throwable cause) {
	        super(message, cause);
	    }
	    public HttpClientException(Throwable cause) {
	        super(cause);
	    }
	    /**
	     * Constructs a new exception with the specified detail message,
	     * cause, suppression enabled or disabled, and writable stack
	     * trace enabled or disabled.
	     *
	     * @param  message the detail message.
	     * @param cause the cause.  (A {@code null} value is permitted,
	     * and indicates that the cause is nonexistent or unknown.)
	     * @param enableSuppression whether or not suppression is enabled
	     *                          or disabled
	     * @param writableStackTrace whether or not the stack trace should
	     *                           be writable
	     * @since 1.7
	     */
	    protected HttpClientException(String message, Throwable cause,
	                        boolean enableSuppression,
	                        boolean writableStackTrace) {
	        super(message, cause, enableSuppression, writableStackTrace);
	    }
	}
	
	public static void main(String[] args) {
	}
}
