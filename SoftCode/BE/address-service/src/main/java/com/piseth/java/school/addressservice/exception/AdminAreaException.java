package com.piseth.java.school.addressservice.exception;

public class AdminAreaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminAreaException(final String message) {
		super(message);
		//Call the constructor of the parent class and pass it the message 
	}
	
	public AdminAreaException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
