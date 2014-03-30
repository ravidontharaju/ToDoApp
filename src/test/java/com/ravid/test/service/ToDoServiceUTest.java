package com.ravid.test.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ravid.main.entity.ToDoEntity;
import com.ravid.main.exception.ToDoException;
import com.ravid.main.repository.ToDoRepository;
import com.ravid.main.service.ToDoService;
import com.ravid.main.service.TwilioService;
import com.ravid.test.ToDoAppTestConfiguration;
import com.twilio.sdk.TwilioRestException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ToDoAppTestConfiguration.class })
public class ToDoServiceUTest {

	private ToDoService underTest;

	@Autowired
	private ToDoRepository toDoRepositoryMock;

	@Autowired
	private TwilioService twilioServiceMock;

	@Mock
	private ToDoEntity toDoEntityMock;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		when(toDoEntityMock.getTitle()).thenReturn("dummy title");
		when(toDoEntityMock.getBody()).thenReturn("dummy body");
		when(toDoEntityMock.isDone()).thenReturn(false);
		when(toDoRepositoryMock.findByTitle("dummy title")).thenReturn(
				toDoEntityMock);
		
		underTest = new ToDoService();
		underTest.setToDoRepository(toDoRepositoryMock);
		underTest.setTwilioService(twilioServiceMock);
	}

	@Test
	public void testGet() {
		ToDoEntity toDoEntity = underTest.get("dummy title");

		assertEquals("dummy title", toDoEntity.getTitle());
		assertEquals("dummy body", toDoEntity.getBody());
		assertEquals(false, toDoEntity.isDone());
	}

	@Test
	public void testDelete() {
		underTest.delete("dummy title");

		verify(toDoRepositoryMock, times(1)).delete(toDoEntityMock);
	}

	@Test(expected = ToDoException.class)
	public void testSaveWithException() throws ToDoException {
		underTest.save(toDoEntityMock);

		verify(toDoEntityMock, times(1)).getTitle();
		verify(toDoRepositoryMock, times(1)).findByTitle("dummy title");
		verify(toDoRepositoryMock, times(0)).save(toDoEntityMock);
	}

	@Test
	public void testSave() throws ToDoException {
		when(toDoEntityMock.getTitle()).thenReturn("no more dummy title");
		underTest.save(toDoEntityMock);

		verify(toDoEntityMock, times(1)).getTitle();
		verify(toDoRepositoryMock, times(1)).findByTitle("no more dummy title");
		verify(toDoRepositoryMock, times(1)).save(toDoEntityMock);
	}

	@Test
	public void testUpdateNoSMS() throws TwilioRestException {
		underTest.update(toDoEntityMock);

		verify(toDoRepositoryMock, times(1)).save(toDoEntityMock);
		verify(twilioServiceMock, times(0)).sendMessage(toDoEntityMock);
	}

	@Test
	public void testUpdate() throws TwilioRestException {
		when(toDoEntityMock.isDone()).thenReturn(true);
		underTest.update(toDoEntityMock);

		verify(toDoRepositoryMock, times(1)).save(toDoEntityMock);
		verify(twilioServiceMock, times(1)).sendMessage(toDoEntityMock);
	}

	@Test
	public void testSearch() {
		List<ToDoEntity> toDoEntities = Arrays.asList(toDoEntityMock);
		when(
				toDoRepositoryMock.findByTitleContainingOrBodyContaining(
						"dummy title", "dummy title")).thenReturn(toDoEntities);

		List<ToDoEntity> resultList = underTest.search("dummy title");
		assertFalse(resultList == null || resultList.isEmpty());
		assertEquals(toDoEntityMock, resultList.get(0));
	}

}
