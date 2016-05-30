package com.foscam.test.net.study;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
/**
 * 心跳检测
 * @author Administrator
 *
 */
public class Study02 {

	
	/**
	 * 1）使用socket进行连接，如果启动连接时，目标服务已启动，则能够连接，
	 * 若连接成功后目标服务停止，那么socket的状态一直是已连接，若要从新建立连接connect提示已连接
	 * 
	 * 
	 * 
	 */
	
	public static void main(String[] args) {
		
		
		
		try {
//		
			Socket socket = new Socket();
			//socket.bind(new InetSocketAddress(InetAddress.getLocalHost(),10000));
			socket.setKeepAlive(true);
			
			//System.out.println(socket.getLocalSocketAddress().toString());
			System.out.println(socket.isConnected());
			//System.out.println(socket.getRemoteSocketAddress().toString());
			System.out.println(socket.isBound());
			System.out.println(socket.getKeepAlive());
			//System.out.println(socket.getReceiveBufferSize());
			//System.out.println(socket.getSendBufferSize());
			socket.connect(new InetSocketAddress("127.0.0.1", 10000));
			System.out.println(socket.isConnected());
			System.out.println(socket.isBound());
			System.out.println(socket.getRemoteSocketAddress().toString());
	
		
			while (true) {
				try {
					
					Thread.sleep(5000);
					
					if(!socket.isConnected()){
						System.out.println("连接失败");
						
					}else{
						socket.sendUrgentData(0xff);
						System.out.println("连接成功");
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						socket = new Socket("127.0.0.1", 10000);
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}
	
	

}
