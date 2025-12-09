package com.piseth.java.school.roomownerservice.exception;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;

public class ApplicationException extends RuntimeException implements ClassifiableError{
	
	private static final long serialVersionUID = -6319905709220563415L;
	
	private final Outcome outcome;

	public ApplicationException(final Outcome outcome, final String message) {
		super(message);
		this.outcome = outcome;
	}
	
	public ApplicationException(final Outcome outcome, final String message, final Throwable cause) {
		super(message, cause);
		this.outcome = outcome;
	}

	@Override
	public Outcome getOutcome() {
		// TODO Auto-generated method stub
		return this.outcome;
	}

}