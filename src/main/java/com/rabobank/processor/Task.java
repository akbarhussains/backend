package com.rabobank.processor;

import java.util.concurrent.Callable;

public class Task implements Callable<String> {

	@Override
	public String call() throws Exception {
	
		for(int i=0;i<100;i++) {
			System.out.println(" "+i);
		}
		return "completed";
	}

	

}
