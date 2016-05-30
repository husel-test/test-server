package com.foscam.test.net.monitor;

public class ThreadUtil {
	
	/**
	 * 获取当前虚拟机的所有线程
	 * @return
	 */
	public static Thread[] findJVMThreads() {
		ThreadGroup group = 
		Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
	
		        // 遍历线程组树，获取根线程组
		while ( group != null ) {
		topGroup = group;
		group = group.getParent();
		}
		        // 激活的线程数加倍
		int estimatedSize = topGroup.activeCount() * 2;
		Thread[] slackList = new Thread[estimatedSize];
		        //获取根线程组的所有线程
		int actualSize = topGroup.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		return list;
	}
	/**
	 * 获取当前进程的所有线程
	 * @return
	 */
	public static Thread[] findCurrentThreads() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		// 激活的线程数加倍
		Thread[] slackList = new Thread[group.activeCount()*2];
		//获取根线程组的所有线程
		int actualSize = group.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		return list;
	} 
	
	public static void main(String[] args) {
		Thread t1 = new Thread(){
			
			@Override
			public void run() {
				
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t1.start();
		Thread t2 = new Thread(){
			
			@Override
			public void run() {
				
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t2.start();
		
		
		Thread[] all = ThreadUtil.findCurrentThreads();
		
		for (Thread thread : all) {
			System.out.println(thread.toString());
		}
		
		
	}
}
