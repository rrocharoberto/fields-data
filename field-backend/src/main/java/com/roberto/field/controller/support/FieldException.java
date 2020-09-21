package com.roberto.field.controller.support;

public class FieldException extends RuntimeException {

	private static final long serialVersionUID = 3989706162555323746L; //automatically generated

	public FieldException() {
	}

	public FieldException(String message) {
		super(message);
	}

	public FieldException(Throwable cause) {
		super(cause);
	}

	public FieldException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
