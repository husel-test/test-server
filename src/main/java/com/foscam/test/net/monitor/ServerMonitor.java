package com.foscam.test.net.monitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * 心跳检测管理控制类
 * @author hujun 2015.05.19
 *
 */
public class ServerMonitor {
	
	private static ServerMonitor serverMonitor = null;
	
	private Set<String> hostWithPorts = new HashSet<String>();
	private Map<String,ServerCheckTask> hostCheckTasks= new HashMap<String,ServerCheckTask>();
	
	private ServerMonitor(){
		//是否启动管理线程？
	}
	public static synchronized ServerMonitor getInstance(){
		
		if(serverMonitor==null){
			serverMonitor = new ServerMonitor();
		}
		return serverMonitor;
	}
	
	/**
	 * 添加对某个服务的监视
	 * @param host IP或者域名
	 * @param port 端口
	 * @param hearbeatTime 监视发送心跳时间间隔，单位毫秒
	 */
	public synchronized void add(String host,int port,long hearbeatTime){
		
		String hostWithPort = this._getAndCheckHostWithPort(host, port);
		
		if(!hostWithPorts.contains(hostWithPort)){
		   //新增监控	
			hostWithPorts.add(hostWithPort);
			ServerCheckTask task = new ServerCheckTask(host,port,hearbeatTime);
			task.start();
			hostCheckTasks.put(hostWithPort, task);
			
		}else {
		   //检测旧的是否在运行
			ServerCheckTask task = hostCheckTasks.get(hostWithPorts);
			if(task==null||!task.isAlive()){
				
				task =  new ServerCheckTask(host,port,hearbeatTime);
				task.start();
				hostCheckTasks.put(hostWithPort, task);
			}
			
		}
		
	}
	/**
	 * 添加对某个服务的监视
	 * @param host IP或者域名
	 * @param port 端口
	 */
	public synchronized void add(String host,int port){
		
		this.add(host, port, ServerCheckTask.DEFAULT_HEARTBEAT);
	}
	
	/**
	 * 取消对某个服务的监视
	 * @param host IP或者域名
	 * @param port 端口
	 */
	public synchronized void del(String host,int port){
		
		String hostWithPort = this._getAndCheckHostWithPort(host, port);
		
		if(hostWithPorts.contains(hostWithPort)){
			   
			ServerCheckTask task = hostCheckTasks.get(hostWithPort);
			
			hostWithPorts.remove(hostWithPort);
			hostCheckTasks.remove(hostWithPort);
			
			if(task!=null&&task.isAlive()){
				task.stopCheck();
			}
				
		}
		
		
		
	}
	
	
	private String _getAndCheckHostWithPort(String host,int port){
		
		if(host==null||port<=0||host.length()==0){
			throw new IllegalArgumentException(host+" , "+port);
		}
		return host+":"+port;
	}
	
	
	public static void main(String[] args) {
		
		
		try {
			ServerMonitor monitor = ServerMonitor.getInstance();
			
			monitor.add("172.16.30.32", 40002);//portal
			
			monitor.add("172.16.30.36", 60000);//fcmall
			
			monitor.add("127.0.0.1", 10000);//account
			
//			Thread.sleep(65000);
//			
//			monitor.del("127.0.0.1", 10000);
//			
//			Thread.sleep(65000);
//			
//			monitor.add("127.0.0.1", 10000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
}
