package com.foscam.test.net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Test {
	
	
	public static void main(String[] args) {
		
		
		Date date = new Date(1429153647000L);
		Date date1 = new Date(1431745647000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		System.out.println(sdf.format(date));
		System.out.println(sdf.format(date1));
		
		char c = '\0';
		char c1 = '\u0000';
		System.out.println(c==c1);
		System.out.println(c);
		
		String s = c +"";
		char c2 = ' ';
		System.out.println((int)c1);
		System.out.println((int)c2);
		System.out.println(" ".equals(s));
		
		System.out.println("--------");
		
		System.out.println(Integer.toHexString(c2));
		System.out.println(Integer.toOctalString(c2));//8进制
		
		char c3 = '中';
		System.out.println(Integer.toHexString(c3));
		
		char c4 = '\u4e2d';
		
		System.out.println(c4);
		
		System.out.println("------------>>>>>>>>>>>>>>>>>>>>>>");
		try {
			String ss = "a_1总";
			byte[] buf = ss.getBytes("utf-8");
			System.out.println(Arrays.toString(buf));
			System.out.println(URLEncoder.encode(ss, "utf-8"));
			
			for (int i = 0; i < ss.length(); i++) {
				char cc = ss.charAt(i);
				System.out.println(Integer.toHexString(cc));
			}
			for (int i = 0; i < buf.length; i++) {
				System.out.println(Integer.toHexString(buf[i]));
			}
			
			char cc1 = '\u603b';
			System.out.println(cc1);
			char cc2 = '\u0061';
			System.out.println(cc2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
