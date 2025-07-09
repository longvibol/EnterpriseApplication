package com.piseth.java.school.roomservice.exception;

public class RoomNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(String id) {
		super("Room is not found with ID : "+id);
	}

}
