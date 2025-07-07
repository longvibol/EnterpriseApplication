package com.piseth.java.school.roomservice.exception;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ProblemDetail> handleConstraintViolation(WebExchangeBindException ex, ServerWebExchange exchange){
		log.warn("Constrain Violation: {}",ex.getMessage());
		
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problemDetail.setTitle("Constrain Violation");
		problemDetail.setInstance(URI.create("https://www.google.com"));
		return Mono.just(problemDetail);
	}
}
