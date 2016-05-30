package com.foscam.test.thread;

import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {
	
	public static void main(String[] args) {
		
		
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(5);
		
		System.out.println(queue.offer("2"));
		System.out.println(queue.offer("3"));
		System.out.println(queue.offer("4"));
		System.out.println(queue.offer("1"));
		System.out.println(queue.offer("b"));
		System.out.println(queue.offer("a"));
		
		System.out.println(queue.element());
		
		System.out.println(queue.toString());
	}

}
