package com.foscam.test.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum SingletonTest {
	
	INSTACNE;
	
	private int a;
	
	private String b;
	
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		SingletonTest s = SingletonTest.INSTACNE;
		s.init();
		
		System.out.println(s.a);
		System.out.println(s.b);
		
		Constructor<SingletonTest> c = SingletonTest.class.getDeclaredConstructor();
		SingletonTest s1 = c.newInstance();
		
		System.out.println(s==s1);
	}
	public void init(){
		
		a = 10;
		
		b = "aaa";
		
		System.out.println("..........");
	}
	
	
}
