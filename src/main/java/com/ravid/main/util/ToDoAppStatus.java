package com.ravid.main.util;

public enum ToDoAppStatus {

	SUCCESS("success", 200), 
	FAIL("fail", 400), 
	ERROR("error", 404);

	private String status;
	private int response;

	private ToDoAppStatus(String status, int response) {
		this.status = status;
		this.response = response;
	}

	public String getStatus() {
		return this.status;
	}

	public static ToDoAppStatus toStatus(int response) {
		for (ToDoAppStatus toDoAppStatus : ToDoAppStatus.values()) {
			if (toDoAppStatus.response == response) {
				return toDoAppStatus;
			}
		}
		// If no matching status found for response then return ERROR.
		return ERROR;
	}
}
