package com.ravid.main.controller;

import static com.ravid.main.service.ToDoValidationService.validateInput;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.twilio.sdk.TwilioRestException;

@Path("/todo")
@Component
public class ToDoController {

	@Autowired
	private ToDoService toDoService;

	@GET
	@Path("/get/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public ToDoEntity get(@PathParam("title") String title) {
		ToDoEntity toDoEntity = toDoService.get(title);
		return toDoEntity;
	}

	@GET
	@Path("/search/{text}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ToDoEntity> search(@PathParam("text") String text) {
		List<ToDoEntity> toDoEntities = toDoService.search(text);
		return toDoEntities;
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(JSONObject jsonObject) throws JSONException {

		String message = validateInput(jsonObject);

		if (message.isEmpty()) {
			return Response.status(400).entity(message).build();
		}

		ToDoEntity toDoEntity = convertToEntity(jsonObject);
		String output = "";
		try {
			toDoService.save(toDoEntity);
			output = "ToDo task with title : " + toDoEntity.getTitle()
					+ " saved.";
		} catch (ToDoException exception) {
			output = exception.getMessage();
		}
		return Response.status(200).entity(output).build();
	}

	@DELETE
	@Path("/delete/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("title") String title) {

		toDoService.delete(title);
		String output = "ToDo task with title : " + title
				+ " has been deleted.";
		return Response.status(200).entity(output).build();
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(JSONObject jsonObject) throws JSONException,
			TwilioRestException {

		String message = validateInput(jsonObject);

		if (!message.isEmpty()) {
			return Response.status(400).entity(message).build();
		}

		ToDoEntity toDoEntity = convertToEntity(jsonObject);
		toDoService.update(toDoEntity);
		String output = "ToDo task with title : " + toDoEntity.getTitle()
				+ " has been updated.";
		return Response.status(200).entity(output).build();
	}

	private ToDoEntity convertToEntity(JSONObject jsonObject)
			throws JSONException {
		ToDoEntity toDoEntity = new ToDoEntity();
		toDoEntity.setTitle(jsonObject.getString("title"));
		toDoEntity.setBody(jsonObject.getString("body"));
		toDoEntity.setDone(jsonObject.getBoolean("done"));
		return toDoEntity;
	}

	public void setToDoService(ToDoService toDoService) {
		this.toDoService = toDoService;
	}

}
