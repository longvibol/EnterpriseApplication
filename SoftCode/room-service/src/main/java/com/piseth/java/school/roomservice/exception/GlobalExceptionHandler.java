package com.piseth.java.school.roomservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Hidden
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private final ProblemDetailFactory problemFactory;
	
	@ExceptionHandler(RoomNotFoundException.class)
	public Mono<ProblemDetail> handleRoomNotFound(RoomNotFoundException ex, ServerWebExchange exchange){
		log.warn("Room Not Found: {}",ex.getMessage());
		return Mono.just(problemFactory.create(
				HttpStatus.NOT_FOUND, 
				ex.getMessage(), 
				ErrorCode.ROOM_NOT_FOUND.name(), 
				exchange));
	}
	
	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ProblemDetail> handleConstraintViolation(WebExchangeBindException ex, ServerWebExchange exchange){
		log.warn("Constrain Violation: {}",ex.getMessage());		
		return Mono.just(problemFactory.create(
				HttpStatus.BAD_REQUEST, 
				ex.getMessage(), 
				ErrorCode.CONSTRAIN_VIOLATION.name(), 
				exchange));
	}
	
	@ExceptionHandler(Exception.class)
	public Mono<ProblemDetail> handleGeneric(Exception ex, ServerWebExchange exchange){
		log.warn("Unexpected Erro: {}",ex.getMessage());		
		return Mono.just(problemFactory.create(
				HttpStatus.INTERNAL_SERVER_ERROR, 
				"Unexpected error: "+ex.getMessage(), 
				ErrorCode.SYSTEM_ERROR.name(), 
				exchange));
	}
	
}
