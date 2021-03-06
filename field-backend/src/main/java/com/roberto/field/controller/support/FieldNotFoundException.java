package com.roberto.field.controller.support;

public class FieldNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8698263187701278577L; // generated

	public FieldNotFoundException() {
	}

	public FieldNotFoundException(String message) {
		super(message);
	}

	public FieldNotFoundException(Throwable cause) {
		super(cause);
	}

	public FieldNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
