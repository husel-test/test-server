package com.foscam.test.demo;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.ObjectUtils;

public class DateDemo {
	
	public static void main(String[] args) {
		
		String t = "aaaa";
		System.out.println(t.compareTo("111a"));
		System.out.println(t.hashCode());
		
		System.out.println("" instanceof String);
		System.out.println(null instanceof String);
		
		
		System.out.println((1 & 4));
		
		int a= 1 << 4;
		printInfo(a);
		System.out.println(Math.pow(2, 4));
		
		printInfo(1);
		printInfo(1>>1);
		printInfo(1<<1);
		printInfo(10);
		printInfo(10>>1);
		printInfo(10<<1);
		
		Date date  = new Date(1448444060L*1000);
		
		String s = DateUtil.formatDate(date,"yyyy-MM-dd HH:mm:ss");
		
		System.out.println(s);
	}
	
	public static void printInfo(int i){
		System.out.println(i+","+Integer.toBinaryString(i));
		
	}

}
