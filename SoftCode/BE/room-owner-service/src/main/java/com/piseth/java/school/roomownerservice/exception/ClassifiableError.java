package com.piseth.java.school.roomownerservice.exception;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;

public interface ClassifiableError {

	Outcome getOutcome();
}