package com.ravid.test.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ravid.main.entity.ToDoEntity;
import com.ravid.main.service.TwilioService;
import com.ravid.test.ToDoAppTestConfiguration;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Sms;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ToDoAppTestConfiguration.class })
public class TwilioServiceUTest {

	private TwilioService underTest;

	@Value("${twilio.from}")
	private String from;
	
	@Value("${twilio.to}")
	private String to;
	
	@Mock
	private TwilioRestClient twilioRestClientMock;

	@Mock
	private Account accountMock;

	@Mock
	private SmsFactory messageFactoryMock;

	@Mock
	private Sms messageMock;

	@Mock
	private ToDoEntity toDoEntityMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(messageMock.getSid()).thenReturn("dummy sid");
		when(accountMock.getSmsFactory()).thenReturn(messageFactoryMock);
		when(twilioRestClientMock.getAccount()).thenReturn(accountMock);

		when(toDoEntityMock.getTitle()).thenReturn("dummy title");
		when(toDoEntityMock.getBody()).thenReturn("dummy body");
		when(toDoEntityMock.isDone()).thenReturn(false);

		underTest = new TwilioService();
		underTest.setTwilioRestClient(twilioRestClientMock);
		underTest.setFrom(from);
		underTest.setTo(to);
	}

	@Test
	public void testSendMessage() throws TwilioRestException {
		Map<String, String> params = underTest.getParams();
		when(messageFactoryMock.create(params)).thenReturn(messageMock);
		
		String actual = underTest.sendMessage(toDoEntityMock);
		assertEquals("SMS with ID : dummy sid has been sent.", actual);
	}

}
