package com.ravid.main.controller;

import static com.ravid.main.service.ToDoValidationService.validateInput;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ravid.main.entity.ToDoEntity;
import com.ravid.main.exception.ToDoException;
import com.ravid.main.service.ToDoService;
import com.ravid.main.util.ToDoAppStatus;
import com.twilio.sdk.TwilioRestException;

@Path("/todo")
@Component
public class ToDoController {

	@Autowired
	private ToDoService toDoService;

	@GET
	@Path("/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public ToDoEntity getOne(@PathParam("title") String title) {
		ToDoEntity toDoEntity = toDoService.get(title);
		return toDoEntity;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ToDoEntity> getAll() {
		List<ToDoEntity> toDoEntities = toDoService.getAll();
		return toDoEntities;
	}

	@GET
	@Path("/search/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ToDoEntity> search(@PathParam("text") String text) {
		List<ToDoEntity> toDoEntities = toDoService.search(text);
		return toDoEntities;
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(JSONObject jsonObject) throws JSONException {

		String message = validateInput(jsonObject);

		if (!message.isEmpty()) {
			String output = buildJsonResponse(ToDoAppStatus.FAIL, message);
			return Response.status(400).entity(output).build();
		}

		ToDoEntity toDoEntity = convertToEntity(jsonObject);
		String output;
		int response;
		try {
			toDoService.save(toDoEntity);
			output = "ToDo task with title : " + toDoEntity.getTitle()
					+ " saved.";
			response = 200;
		} catch (ToDoException exception) {
			output = exception.getMessage();
			response = 400;
		}
		output = buildJsonResponse(ToDoAppStatus.toStatus(response), output);
		return Response.status(response).entity(output).build();
	}

	@DELETE
	@Path("/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("title") String title) {

		toDoService.delete(title);
		String output = buildJsonResponse(ToDoAppStatus.SUCCESS,
				"ToDo task with title : " + title + " has been deleted.");
		return Response.status(200).entity(output).build();
	}

	@PUT
	@Path("/{title}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("title") String title,
			JSONObject jsonObject) {

		ToDoEntity toUpdate = toDoService.get(title);
		ToDoEntity partialToDoEntity = convertToPartialEntity(jsonObject);

		if (null != partialToDoEntity.getBody()) {
			toUpdate.setBody(partialToDoEntity.getBody());
		}

		if (null != partialToDoEntity.isDone()) {
			toUpdate.setDone(partialToDoEntity.isDone());
		}

		String output;
		int response;
		try {
			toDoService.update(toUpdate);
			output = "ToDo task with title : " + toUpdate.getTitle()
					+ " has been updated.";
			response = 200;
		} catch (TwilioRestException e) {
			output = e.getErrorMessage()
					+ ". Unable to send Text Notification.";
			response = 400;
		} catch (RuntimeException e) {
			output = "Received a Runtime exception: " + e.getLocalizedMessage()
					+ ". Unable to send Text Notification.";
			response = 400;
		}
		output = buildJsonResponse(ToDoAppStatus.toStatus(response), output);
		return Response.status(response).entity(output).build();
	}

	private ToDoEntity convertToEntity(JSONObject jsonObject)
			throws JSONException {
		ToDoEntity toDoEntity = new ToDoEntity();
		toDoEntity.setTitle(jsonObject.getString("title"));
		toDoEntity.setBody(jsonObject.getString("body"));
		toDoEntity.setDone(jsonObject.getBoolean("done"));
		return toDoEntity;
	}

	private ToDoEntity convertToPartialEntity(JSONObject jsonObject) {
		ToDoEntity partialToDoEntity = new ToDoEntity();
		try {
			partialToDoEntity.setBody(jsonObject.has("body") ? jsonObject
					.getString("body") : null);
			partialToDoEntity.setDone(jsonObject.has("done") ? jsonObject
					.getBoolean("done") : null);
		} catch (JSONException e) {
			// Do nothing, this is expected.
		}
		return partialToDoEntity;
	}

	public void setToDoService(ToDoService toDoService) {
		this.toDoService = toDoService;
	}

	public String buildJsonResponse(ToDoAppStatus status, String message) {
		StringBuilder jsonString = new StringBuilder("{ " + "\"status\" : \""
				+ status.getStatus() + "\", " + "\"message\" : \"" + message
				+ "\"" + "}");

		return jsonString.toString();
	}

}
