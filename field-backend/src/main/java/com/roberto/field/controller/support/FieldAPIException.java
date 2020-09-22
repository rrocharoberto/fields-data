package com.roberto.field.controller.support;

public class FieldAPIException extends RuntimeException {

	private static final long serialVersionUID = 8698263187701278577L; // generated

	public FieldAPIException() {
	}

	public FieldAPIException(String message) {
		super(message);
	}

	public FieldAPIException(Throwable cause) {
		super(cause);
	}

	public FieldAPIException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldAPIException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
