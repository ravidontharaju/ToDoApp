package com.ravid.main.service;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ToDoValidationService {

	public static String validateInput(JSONObject jsonObject) {

		StringBuilder builder = new StringBuilder("Missing field(s): ");
		boolean isValid = true;

		if (!jsonObject.has("title")) {
			builder.append(" title");
			isValid = false;
		} else {
			try {
				isValid = jsonObject.getString("title").isEmpty() ? false
						: true;
			} catch (JSONException e) {
				isValid = false;
			}
		}

		if (!jsonObject.has("body")) {
			builder.append(" body");
			isValid = false;
		}

		if (!jsonObject.has("done")) {
			builder.append(" done");
			isValid = false;
		}

		return isValid ? "" : builder.toString();
	}

}