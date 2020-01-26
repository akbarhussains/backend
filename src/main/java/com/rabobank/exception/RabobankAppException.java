package com.rabobank.exception;

public class RabobankAppException extends Exception{

	
	public RabobankAppException(String message) {
		super(message);
	}
	
	public RabobankAppException(Exception e) {
		super(e);
	}
	
	public RabobankAppException(String message,Exception e) {
		super(message,e);
	}
	
	public String toString() {
		return this.getMessage();
	}
	
}
