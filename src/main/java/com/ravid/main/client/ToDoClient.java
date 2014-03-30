package com.ravid.main.client;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ToDoClient {

	public static void main(String[] args) {

		try {

			Map<String, Object> map = new HashMap<>();
			map.put("title", "to do task one");
			map.put("body", "body is to describe something");
			map.put("done", "false");

			JSONObject st = new JSONObject(map);

			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(

			JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client

			.resource("http://localhost:8080/ToDoApp/rest/todo/save");

			ClientResponse response = webResource.accept("application/json")

			.type("application/json").post(ClientResponse.class, st);

			if (response.getStatus() != 200) {

				throw new RuntimeException("Failed : HTTP error code : "

				+ response.getStatus());

			}

			String output = response.getEntity(String.class);

			System.out.println("Server response .... \n");

			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}