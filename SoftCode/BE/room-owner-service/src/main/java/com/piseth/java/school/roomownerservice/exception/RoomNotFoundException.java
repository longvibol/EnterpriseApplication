package com.piseth.java.school.roomownerservice.exception;

public class RoomNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(String id) {
		super("Room is not found with ID : "+id);
	}

}
