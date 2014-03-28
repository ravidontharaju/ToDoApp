package com.ravid.client;

import com.ravid.entity.ToDoEntity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ToDoClient {

	   public static void main(String[] args) {

		           try {

		               ToDoEntity st = new ToDoEntity("Adriana", "Barrer", false);

		               ClientConfig clientConfig = new DefaultClientConfig();

		               clientConfig.getFeatures().put(

		                       JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		               Client client = Client.create(clientConfig);

		               WebResource webResource = client

		                       .resource("http://localhost:8080/ToDoApp/rest/todo/send");

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
