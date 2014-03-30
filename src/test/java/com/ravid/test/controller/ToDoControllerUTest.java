package com.ravid.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
		
		when(toDoServiceMock.get("dummy title")).thenReturn(toDoEntityMock);
		when(toDoServiceMock.search("dummy search")).thenReturn(Arrays.asList(toDoEntityMock));
		
		
		underTest = new ToDoController();
		underTest.setToDoService(toDoServiceMock);
	}

	@Test
	public void testGet() {
		ToDoEntity toDoEntity = underTest.get("dummy title");
		
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
		assertEquals("ToDo task with title : dummy title saved.", response.getEntity());
	}

	@Test
	public void testDelete() {
		Response response = underTest.delete("dummy title");
		
		verify(toDoServiceMock, times(1)).delete("dummy title");
		assertEquals(200, response.getStatus());
		assertEquals("ToDo task with title : dummy title has been deleted.", response.getEntity());
	}

	@Test
	public void testUpdate() throws JSONException, TwilioRestException {
		Response response = underTest.update(jsonObjectMock);
		
		assertEquals(200, response.getStatus());
		assertEquals("ToDo task with title : dummy title has been updated.", response.getEntity());
	}

}
