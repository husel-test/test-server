package com.foscam.test.net.study;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Dgram {
	
	private final static String UTF8 = "UTF-8";
	
	public static DatagramPacket toDatagram(String s, InetAddress destIA,
			int destPort) throws UnsupportedEncodingException {
		byte[] buf = s.getBytes(UTF8);
		return new DatagramPacket(buf, buf.length, destIA, destPort);
	}

	public static String toString(DatagramPacket p) throws UnsupportedEncodingException {
		return new String(p.getData(), 0, p.getLength(),UTF8);
	}
	
	public static void main(String[] args) {
		String s = "a_1总";
		try {
			
			DatagramPacket dataPacket = toDatagram(s, InetAddress.getLocalHost(), 9989);
			System.out.println(dataPacket.toString());
			System.out.println(dataPacket.getLength());
			System.out.println(Arrays.toString(dataPacket.getData()));
			System.out.println(URLEncoder.encode(s, UTF8));
			byte[] buf = dataPacket.getData();
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				System.out.println(Integer.toHexString(c));
			}
			for (int i = 0; i < buf.length; i++) {
				System.out.println(Integer.toHexString(buf[i]));
			}
			//Integer.toHexString();
			
			
			System.out.println("===========================");
			System.out.println(toString(dataPacket));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}