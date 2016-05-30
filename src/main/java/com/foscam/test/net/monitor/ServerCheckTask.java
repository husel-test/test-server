package com.foscam.test.net.monitor;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳检测任务运行类
 * @author hujun 2015.05.19
 *
 */
public class ServerCheckTask extends Thread{
	
	public final static long MAX_HEARTBEAT= 1000*60*60*24L;//心跳检测最大间隔,1天
	public final static long MIN_HEARTBEAT = 1000*5L;//心跳检测最小间隔，5秒
	public final static long DEFAULT_HEARTBEAT = 1000*60L;//默认心跳检测间隔,1分钟
	private static int maxErrorTimes = 3;//检测服务累积出现错误允许最大次数，超过此次数则发出警报
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private String host ;//目标主机IP或者域名
	private int port ;//目标服务端口
	
	private long heartbeatTime = DEFAULT_HEARTBEAT;//发送检测心跳周期，默认为1分钟
	private int failedTimes = 0;
	
	private boolean serverRunning = false;//监控的服务对象的运行状态，false表示监控的服务没有正常运行
	private boolean check = true;//本监控程序控制按钮，false则关闭此线程，不再对某个服务进行监控
	
	public ServerCheckTask(String host, int port){
		
		this.host = host;
		this.port = port;
		this.setName(host+":"+port);
		this.setPriority(MIN_PRIORITY);
	}
	
	public ServerCheckTask(String host, int port,long heartbeatTime){
		
		this(host, port);
		this.setHeartbeatTime(heartbeatTime);
	}
	
	public boolean isServerRunning(){
		
		return serverRunning;
	}
	
	public void stopCheck(){
		
		this.check = false;
		this.interrupt();
	}
	public boolean isCheck(){
		
		return this.check;
	}
	public void setHeartbeatTime(long heartbeatTime){
		if(heartbeatTime<MIN_HEARTBEAT){
			heartbeatTime = MIN_HEARTBEAT;
		}
		if(heartbeatTime>MAX_HEARTBEAT){
			heartbeatTime = MAX_HEARTBEAT;
		}
		this.heartbeatTime = heartbeatTime;
		
	}
	public long getHeartbeatTime(){
		
		return this.heartbeatTime;
	}
	public int getFailedTimes(){
		
		return this.failedTimes;
	}

	@Override
	public void run() {
		
		Socket socket = null;
		while(true){
			
			try {
				
				if(socket==null){
					socket = new Socket(host, port);
				}
				if(socket.isConnected()){
					try {
						//发送心跳
						socket.sendUrgentData(0xFF);
						
						//发送成功
						serverRunning = true;
						failedTimes = 0;
						
						if(log.isDebugEnabled()){
							log.debug("[{}] heartbeat check success! ",this.getName());
						}
						
					//发送心跳报文出错，服务器中途关闭	
					} catch (IOException e) {
						log.warn("[{}] send headbeat msg error! {} ",this.getName(),e.getMessage());
						
						this.closeSocket(socket);
						socket = null;
						this.processError();
					}
				}
			} catch (Exception e) {
				//创建socket连接异常，服务还没有启动
				log.error("[{}] can not connect! please check the server is running or the network is unblocked . {} ",this.getName(),e.getMessage());
				
				this.closeSocket(socket);
				socket = null;
				this.processError();
				
			}
			
			try{
				
				Thread.sleep(heartbeatTime);
			} catch (InterruptedException e){	
				log.error("[{}] stop to check! {}",this.getName(),e.getMessage());
			}
			
			//停止监控服务
			if(!check){
				break;
			}
			
		}
		log.warn("[{}] stoped check! ",this.getName());
		
	}
	
	private void processError(){
		
		//错误处理，业务逻辑在此处添加
		failedTimes ++;
		if(failedTimes>=maxErrorTimes){
			
			log.error("[{}] heartbeat health check error times {}, go to send warn email  ",this.getName(),failedTimes);
			
			serverRunning = false;
		}
	}
	
	
	
	
	private void closeSocket(Socket socket){
		
		if(socket!=null&&!socket.isClosed()){
			try {
				socket.close();
			} catch (IOException e1) {
				log.error("[{}] close socket error! {}",this.getName(),e1.getMessage());
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		
		//监控
		ServerCheckTask t1 = new ServerCheckTask("172.16.30.35",40002);
		t1.start();
		
		ServerCheckTask t2 = new ServerCheckTask("172.16.30.35",10000);
		t2.start();
		
		try {
			Thread.sleep(2000);
			
			System.out.println(Thread.activeCount());
			System.out.println(Thread.currentThread().getName());
			
//			Thread[] all = ThreadUtil.findCurrentThreads();
//			for (int i = 0; i < all.length; i++) {
//				System.out.println(all[i].toString());
//				System.out.println(all[i].getName()+"  "+all[i].isAlive());
//			}
			
			
			t1.stopCheck();
			
			while(true){
				
				Thread.sleep(3000);
				System.out.println(t1.getName()+"  "+t1.isAlive()+"  "+t1.isServerRunning());
				System.out.println(t2.getName()+"  "+t2.isAlive()+"  "+t2.isServerRunning());
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
