package com.foscam.test.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadPoolTest {
	
	public static void main(String[] args) {
		
		
		Executors.newSingleThreadExecutor();
		
		
		Executors.newFixedThreadPool(3);
		
		Executors.newCachedThreadPool();
		
		
		Executors.newScheduledThreadPool(3);
		
		
		ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

			
	}

}
