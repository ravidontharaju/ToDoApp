package com.ravid.main.exception;

public class ToDoException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = null;

	public ToDoException() {
		super();
	}

	public ToDoException(String message) {
		super(message);
		this.message = message;
	}

	public ToDoException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
