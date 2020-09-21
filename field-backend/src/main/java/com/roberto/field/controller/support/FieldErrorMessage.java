package com.roberto.field.controller.support;

public class FieldErrorMessage {

	private int status;
	private String message;
	private long timestamp;

	public FieldErrorMessage(int status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = System.currentTimeMillis();
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
