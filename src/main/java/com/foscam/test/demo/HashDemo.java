package com.foscam.test.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;

public class HashDemo {
	
	private String code ;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	public static void main(String[] args) {
		
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Random ran = new Random();
		
		for (int a = 0;a<10000000;a++) {
			String uuid = UUID.randomUUID().toString();
			//int uidHash = Math.abs(uuid.hashCode())%163;
			int uidHash = Math.abs(ran.nextInt())%100;
			Integer old = map.get(uidHash);
			if(old == null){
				map.put(uidHash, 1);
			}else{
				map.put(uidHash, old+1);
			}
			
		}
		System.out.println("...........");
		int min = Integer.MAX_VALUE;
		int max = 0;
		for (Integer key : map.keySet()) {
			
			int value = map.get(key);
			System.out.println(key+" : "+value);
			
			if(value < min){
				min = value;
			}
			if(value > max){
				max = value;
			}
		}
		System.out.println("min:"+min+"; max:"+max);
		
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HashDemo other = (HashDemo) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	

}
