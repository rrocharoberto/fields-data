package com.roberto.field.controller.support;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

	public GeneralExceptionHandler() {

	}

	/**
	 * Handle general exceptions.
	 * @param ex Any exception
	 * @return A well defined response message
	 */
	@ExceptionHandler
	public ResponseEntity<FieldErrorMessage> handleException(Exception ex) {
		ex.printStackTrace();
		
		FieldErrorMessage error = new FieldErrorMessage(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage());
		
		return new ResponseEntity<FieldErrorMessage>(error, HttpStatus.BAD_REQUEST);
	}
}
