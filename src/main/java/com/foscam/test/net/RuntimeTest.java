package com.foscam.test.net;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
 
public class RuntimeTest {
	
	
    public static void main(String[] args) {
        try {
            // System信息，从jvm获取
            System.out.println("JVM----------------------------------");
            
            Properties properties = System.getProperties();  
	        Set<String> set = properties.stringPropertyNames(); //获取java虚拟机和系统的信息。  
	        for(String name : set){  
	            System.out.println(name + ":" + properties.getProperty(name));  
	        }  
	        System.out.println("System-------------------------------------------");
	        
	        Map<String, String> map = System.getenv();
	        for(String key : map.keySet()){  
	            System.out.println(key + ":" + map.get(key));  
	        }  
            
            
          
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
 
   
}