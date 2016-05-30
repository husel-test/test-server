package com.foscam.test.mytest;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceHignConcurrenceTest {
	
	private static ExecutorService pool = Executors.newFixedThreadPool(100);
	
	
	public static void main(String[] args) throws InterruptedException {
		
		final long t1 = System.currentTimeMillis();
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println((System.currentTimeMillis() -t1)+" ms,"+TestJob.num);
			}
		}, 1, 1, TimeUnit.SECONDS);
		
		
		
		for (int i = 0; i < 10000000; i++) {
			pool.execute(new TestJob());
		}
		
		Thread.sleep(10000);
		pool.shutdown();
		
		if(!pool.isTerminated()){
			Thread.sleep(100);
		}
		
		System.out.println((System.currentTimeMillis() -t1) +" ms");
		
	}

}
class TestJob implements Runnable {
	
	public static Integer num = 0;
	
	public static String sync = "";

	@Override
	public void run() {
		
		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public synchronized static void test() throws InterruptedException{
		
		String name = Thread.currentThread().getName();
		num++;
		if(num%100000==0){
			System.out.println(name+"--->"+num);
		}
		//Thread.sleep(10);
		
	}
	
	
}
