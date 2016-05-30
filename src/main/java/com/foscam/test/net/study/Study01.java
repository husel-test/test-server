package com.foscam.test.net.study;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.net.ServerSocketFactory;

public class Study01 {

	
	
	public static void main(String[] args) {
		
		
		
		try {
//			InetAddress address = InetAddress.getLocalHost();
//			System.out.println(address.getHostName()+","+address.getHostAddress());
//	
//			InetAddress address2 = InetAddress.getByName(address.getHostName());
//			
//			System.out.println(address.equals(address2));
//			
//			InetAddress address3 =InetAddress.getByName(null);
//			System.out.println(address.equals(address3));
//			System.out.println(address3.getHostName()+","+address3.getHostAddress());
//			
//			InetAddress address4 =InetAddress.getByName("localhost");
//			System.out.println(address.equals(address4));
//			System.out.println(address4.getHostName()+","+address4.getHostAddress());
//			System.out.println(Arrays.toString(address4.getAddress()));
//			
//			InetAddress address5 =InetAddress.getByName("127.0.0.1");
//			System.out.println(address.equals(address5));
//			System.out.println(address5.getHostName()+","+address5.getHostAddress());
			
			
			
			
			ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(30001,10);
			System.out.println(server.getLocalPort());
			System.out.println(server.getSoTimeout());
			
			
			DatagramSocket socket = new DatagramSocket(9909);
			
			String s = "aaad1323中国人";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

	}
	
	

}
