package com.rabobank.exception;

/**
 * Application based exception class
 * @author Akbar Hussain
 *
 */
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
