package com.ravid.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.ravid.entity.ToDoEntity;
import com.ravid.service.ToDoService;
import com.twilio.sdk.TwilioRestException;

@Path("/todo")
@Component
public class ToDoController {

	@Autowired
	private ToDoService toDoService;

	@Autowired
	@Qualifier(value="mongoTemplate")
	private MongoOperations mongoOperations;

	@GET
	@Path("/{parameter}")
	public Response responseMsg(@PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {

		String output = "Hello from: " + parameter + " : " + value + " ::::: ";
		if (toDoService != null) {
			output += " :: " + toDoService.get("title").getTitle();

		}

		if (mongoOperations != null) {
			output += " :: " + "mongooooo";
		}

		return Response.status(200).entity(output).build();
	}

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
	public ToDoEntity search(@PathParam("text") String text) {
		ToDoEntity toDoEntity = toDoService.search(text);
		return toDoEntity;
	}
	
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(JSONObject jsonObject) throws JSONException {

		ToDoEntity toDoEntity = new ToDoEntity();
		toDoEntity.setTitle(jsonObject.getString("title"));
		toDoEntity.setBody(jsonObject.getString("body"));
		toDoEntity.setDone(jsonObject.getBoolean("done"));
		toDoService.save(toDoEntity);
		String output = "ToDo task with title : " + toDoEntity.getTitle() + " saved.";
		return Response.status(200).entity(output).build();
	}
	
	@DELETE
	@Path("/delete/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("title") String title) {
		toDoService.delete(title);
		String output = "ToDo task with title : " + title + " has been deleted.";
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(JSONObject jsonObject) throws JSONException, TwilioRestException {

		ToDoEntity toDoEntity = new ToDoEntity();
		toDoEntity.setTitle(jsonObject.getString("title"));
		toDoEntity.setBody(jsonObject.getString("body"));
		toDoEntity.setDone(jsonObject.getBoolean("done"));
		toDoService.update(toDoEntity);
		String output = "ToDo task with title : " + toDoEntity.getTitle() + " has been updated.";
		return Response.status(200).entity(output).build();
	}
	
	

}
