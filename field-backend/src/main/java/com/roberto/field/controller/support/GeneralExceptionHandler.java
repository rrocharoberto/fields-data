package com.roberto.field.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
	
	public GeneralExceptionHandler() {

	}

	/**
	 * Handle general exceptions.
	 * @param ex Any exception
	 * @return A well defined response message
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(Exception ex) {
		logger.info("handleException", ex);
		
		FieldErrorMessage error = new FieldErrorMessage(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getMessage());
		
		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(FieldAPIException ex) {
		logger.info("handleException", ex);
		
		FieldErrorMessage error = new FieldErrorMessage(
				HttpStatus.BAD_GATEWAY.value(),
				ex.getMessage());
		
		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.BAD_GATEWAY);
	}
}
