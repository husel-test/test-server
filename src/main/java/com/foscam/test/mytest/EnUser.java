package com.foscam.test.mytest;

import java.util.Date;

public class EnUser{
	String address;
	String email;
	String household;
	String name;
	Date datatime;
	
	public static void main(String[] args) {
		
		System.out.println("redeyesalvarez@gmail.com".length());
		
		EnUser eu = new EnUser();
		eu.email = "arnaud_machet@hotmail.Com";
		
		EnUser eu2 = new EnUser();
		eu2.email = "arnaud_machet@hotmail.com";
		
		System.out.println(eu.hashCode());
		System.out.println(eu2.hashCode());
		
		System.out.println(eu.equals(eu2));
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.toLowerCase().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnUser other = (EnUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equalsIgnoreCase(other.email))
			return false;
		return true;
	}
	
	
}