package com.ravid.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ravid.main.controller.ToDoController;
import com.ravid.main.entity.ToDoEntity;
import com.ravid.main.service.ToDoService;
import com.ravid.main.util.ToDoAppStatus;
import com.ravid.test.ToDoAppTestConfiguration;
import com.twilio.sdk.TwilioRestException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ToDoAppTestConfiguration.class })
public class ToDoControllerUTest {

	private ToDoController underTest;

	@Autowired
	private ToDoService toDoServiceMock;

	@Mock
	private ToDoEntity toDoEntityMock;

	@Mock
	private JSONObject jsonObjectMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(toDoEntityMock.getTitle()).thenReturn("dummy title");
		when(toDoEntityMock.getBody()).thenReturn("dummy body");
		when(toDoEntityMock.isDone()).thenReturn(false);

		when(jsonObjectMock.getString("title")).thenReturn("dummy title");
		when(jsonObjectMock.getString("body")).thenReturn("dummy body");
		when(jsonObjectMock.getBoolean("done")).thenReturn(false);

		when(jsonObjectMock.has("title")).thenReturn(true);
		when(jsonObjectMock.has("body")).thenReturn(true);
		when(jsonObjectMock.has("done")).thenReturn(true);

		when(toDoServiceMock.get("dummy title")).thenReturn(toDoEntityMock);
		when(toDoServiceMock.search("dummy search")).thenReturn(
				Arrays.asList(toDoEntityMock));

		underTest = new ToDoController();
		underTest.setToDoService(toDoServiceMock);
	}

	@Test
	public void testGet() {
		ToDoEntity toDoEntity = underTest.getOne("dummy title");

		assertEquals("dummy title", toDoEntity.getTitle());
		assertEquals("dummy body", toDoEntity.getBody());
		assertEquals(false, toDoEntity.isDone());
	}

	@Test
	public void testSearch() {
		List<ToDoEntity> resultList = underTest.search("dummy search");

		assertFalse(resultList == null || resultList.isEmpty());
		assertEquals(toDoEntityMock, resultList.get(0));
	}

	@Test
	public void testSave() throws JSONException {
		Response response = underTest.save(jsonObjectMock);

		assertEquals(200, response.getStatus());
		assertTrue(response.getEntity().toString()
				.contains(ToDoAppStatus.SUCCESS.getStatus()));
		assertTrue(response.getEntity().toString()
				.contains("ToDo task with title : dummy title saved."));
		assertEquals(
				"{ \"status\" : \"success\", \"message\" : \"ToDo task with title : dummy title saved.\"}",
				response.getEntity().toString());
	}

	@Test
	public void testSaveMalformed() throws JSONException, TwilioRestException {
		when(jsonObjectMock.has("body")).thenReturn(false);
		when(jsonObjectMock.has("done")).thenReturn(false);

		Response response = underTest.save(jsonObjectMock);

		assertEquals(400, response.getStatus());
		assertTrue(response.getEntity().toString()
				.contains(ToDoAppStatus.FAIL.getStatus()));
		assertTrue(response.getEntity().toString()
				.contains("Missing field(s):  body done"));
	}

	@Test
	public void testDelete() {
		Response response = underTest.delete("dummy title");

		verify(toDoServiceMock, times(1)).delete("dummy title");
		assertEquals(200, response.getStatus());
		assertTrue(response.getEntity().toString()
				.contains(ToDoAppStatus.SUCCESS.getStatus()));
		assertTrue(response
				.getEntity()
				.toString()
				.contains(
						"ToDo task with title : dummy title has been deleted."));
	}

	@Test
	public void testUpdate() throws JSONException, TwilioRestException {
		Response response = underTest.update("dummy title", jsonObjectMock);

		assertEquals(200, response.getStatus());
		assertTrue(response.getEntity().toString()
				.contains(ToDoAppStatus.SUCCESS.getStatus()));
		assertTrue(response
				.getEntity()
				.toString()
				.contains(
						"ToDo task with title : dummy title has been updated."));
	}

	@Test
	public void testBuildJsonResponseForError() {
		String jsonResponse = underTest.buildJsonResponse(ToDoAppStatus.ERROR,
				"Some Error");
		assertTrue(jsonResponse.contains("\"status\" : \"error\""));
		assertTrue(jsonResponse.contains("\"message\" : \"Some Error\""));
	}

}
