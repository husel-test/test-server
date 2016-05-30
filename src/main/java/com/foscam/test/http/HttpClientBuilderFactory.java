package com.foscam.test.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author hujun
 * 
 */
public class HttpClientBuilderFactory {
	private static Logger log = LoggerFactory
			.getLogger(HttpClientBuilderFactory.class);

	private static PoolingHttpClientConnectionManager connPool = null;
	/**
	 * key 为host+port
	 */
	private static HashMap<String, HttpClientBuilder> builders = new HashMap<String, HttpClientBuilder>();

	private static final int MaxTotal = 100;//连接池最大总连接数
	private static final int MaxPreTotal = 20;//到某个HOst，IP+post的最大连接数
	
	private static final int MaxErrorTimes = 1000;
	
	private static final int defaultSocketTimeout = 5000;//毫秒
	
	/**
	 * client执行任务错误次数，超过MaxErrorTimes次，则重置该builder的连接池。
	 */
	private static int failTimes = 0;

	private static RequestConfig defaultRequestConfig = null;
	
	private HttpClientBuilderFactory() {

	}
	static{
		
		  defaultRequestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
				 .setSocketTimeout(defaultSocketTimeout)
	             .setConnectTimeout(defaultSocketTimeout)
	             .setConnectionRequestTimeout(defaultSocketTimeout)
	             .build();
		  
		  createConnectionManager();
	}
	public static RequestConfig getDefaultRequestConfig(){
		
		return defaultRequestConfig;
	}

	/**
	 * 有几个需要注意的地方：
	 * 1.如果你的客户端连接的目标服务器只有一个，那么大可设置最大route连接数和最大连接池连接数相同，以便高效利用连接池中创建的连接。
	 * 2.创建的httpClient对象是线程安全的
	 * ，如果连接的目标服务器只有一个的话，创建一个全局对象即可。一个对象好比开了一个浏览器，多个线程无需每次请求时专门开一个浏览器，统一一个即可。
	 * 3.如果httpClient对象不再使用，记得关闭，释放与服务器保持连接的socket，以便服务器更高效的释放资源。
	 */
	private static void createConnectionManager() {

		HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

			@Override
			public HttpMessageParser<HttpResponse> create(
					SessionInputBuffer buffer, MessageConstraints constraints) {
				LineParser lineParser = new BasicLineParser() {

					@Override
					public Header parseHeader(final CharArrayBuffer buffer) {
						try {
							return super.parseHeader(buffer);
						} catch (ParseException ex) {
							return new BasicHeader(buffer.toString(), null);
						}
					}

				};
				return new DefaultHttpResponseParser(buffer, lineParser,
						DefaultHttpResponseFactory.INSTANCE, constraints) {

					@Override
					protected boolean reject(final CharArrayBuffer line,
							int count) {
						// try to ignore all garbage preceding a status line
						// infinitely
						return false;
					}

				};
			}

		};
		HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

		// Use a custom connection factory to customize the process of
		// initialization of outgoing HTTP connections. Beside standard
		// connection
		// configuration parameters HTTP connection factory can define message
		// parser / writer routines to be employed by individual connections.
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				requestWriterFactory, responseParserFactory);

		// Client HTTP connection objects when fully initialized can be bound to
		// an arbitrary network socket. The process of network socket
		// initialization,
		// its connection to a remote address and binding to a local one is
		// controlled
		// by a connection socket factory.

		// SSL context for secure connections can be created either based on
		// system or application specific properties.
		// SSLContext sslcontext = SSLContexts.createSystemDefault();
		// Use custom hostname verifier to customize SSL hostname verification.
		// X509HostnameVerifier hostnameVerifier = new
		// BrowserCompatHostnameVerifier();

		// Create a registry of custom connection socket factories for supported
		// protocol schemes.
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				// .register("https", new SSLConnectionSocketFactory(sslcontext,
				// hostnameVerifier))
				.build();

		// Use custom DNS resolver to override the system DNS resolution.
		DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

			@Override
			public InetAddress[] resolve(final String host)
					throws UnknownHostException {
				if (host.equalsIgnoreCase("localhost")) {
					return new InetAddress[] { InetAddress
							.getByAddress(new byte[] { 127, 0, 0, 1 }) };
				} else {
					return super.resolve(host);
				}
			}

		};

		// Create a connection manager with custom configuration.
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry, connFactory, dnsResolver);

		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true)
				.setSoKeepAlive(true).setSoTimeout(defaultSocketTimeout)
				.build();
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		connManager.setDefaultSocketConfig(socketConfig);

		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom()
				.setMaxHeaderCount(200).setMaxLineLength(2000).build();
		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints).build();
		// Configure the connection manager to use connection configuration
		// either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);

		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(MaxTotal);
		connManager.setDefaultMaxPerRoute(MaxPreTotal);
		
		// PoolingHttpClientConnectionManager cm = new
		// PoolingHttpClientConnectionManager();
		//
		// //连接池最大生成连接数200
		// cm.setMaxTotal(50);
		// // 默认设置route最大连接数为20
		// cm.setDefaultMaxPerRoute(100);
		// // 指定专门的route，设置最大连接数为80
		// HttpHost host = new HttpHost(hostAddress, port);
		// cm.setMaxPerRoute(new HttpRoute(host), 50);
		// cm.setConnectionConfig(host, ConnectionConfig.DEFAULT);
		
		connPool = connManager ;
	}

	public static synchronized HttpClientBuilder getHttpClientBuilder(
			String host, int port) {

		if (builders.get(host + port) != null) {
			return builders.get(host + port);
		}

		return getHttpClientBuilderAlwaysNew(host, port);
	}

	/**
	 * 创建httpClient生成器httpClientBulider
	 * 
	 * @param hostAddress
	 * @param port
	 * @return
	 */
	public static synchronized HttpClientBuilder getHttpClientBuilderAlwaysNew(
			String host, int port) {
		
		HttpClientBuilder cb = HttpClients.custom().setConnectionManager(connPool);
		builders.put(host + port, cb);
		
		return cb;
	}

	/**
	 * 
	 * @param hostAddress
	 * @param port
	 */
	public static synchronized void updateFailTimes() {

		
		if (failTimes > MaxErrorTimes) {
			log.error(" conn error times ->" + MaxErrorTimes
					+ ",reset conn pool now,pool status:"+ connPool.getTotalStats());
			resetBuliderConnectionManager();
			failTimes = 0;
		} else {
			failTimes++;
			connPool.closeExpiredConnections();
		}

	}

	protected static synchronized void resetBuliderConnectionManager() {
		
		connPool.close();
		
		createConnectionManager();
		
		for (HttpClientBuilder builder : builders.values()) {
			builder.setConnectionManager(connPool);
		}
		
	}
}
