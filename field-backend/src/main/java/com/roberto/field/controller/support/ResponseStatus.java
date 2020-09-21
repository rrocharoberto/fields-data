package com.roberto.field.controller.support;

public class ResponseStatus {

	static private enum STATUS {
		OK
	};

	private int status;
	private String message;

	private ResponseStatus(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public static ResponseStatus success(String message) {
		return new ResponseStatus(STATUS.OK.ordinal(), message);
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
