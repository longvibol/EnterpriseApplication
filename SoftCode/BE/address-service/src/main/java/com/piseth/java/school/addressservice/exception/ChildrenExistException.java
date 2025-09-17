package com.piseth.java.school.addressservice.exception;

public class ChildrenExistException extends AdminAreaException{

	public ChildrenExistException(String code) {
		super("Cannot delete Admin Area which has children" + code);
	}

}
