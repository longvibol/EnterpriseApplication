package com.piseth.java.school.addressservice.exception;

import com.piseth.java.school.addressservice.domain.enumeration.Outcome;

public class ChildrenExistException extends AdminAreaException{

	public ChildrenExistException(String code) {
		super(Outcome.VALIDATION,"Cannot delete Admin Area which has children" + code);
	}

}
