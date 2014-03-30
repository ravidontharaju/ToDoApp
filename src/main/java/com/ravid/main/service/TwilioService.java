package com.ravid.main.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ravid.main.entity.ToDoEntity;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;

@Component
public class TwilioService {

	private static final Logger LOGGER = Logger.getLogger(TwilioService.class
			.getName());

	private static final String ACCOUNT_SID = "ACa1ee2c95a9ab87cba8b30437492a245a";
	private static final String AUTH_TOKEN = "ebeadfe7e671c3630cbbe16e25b0ef5b";
	private static TwilioRestClient twilioRestClient = new TwilioRestClient(
			ACCOUNT_SID, AUTH_TOKEN);
	private static final Map<String, String> params = new HashMap<String, String>();
	
	@Value("${twilio.from}")
	private String from;
	
	@Value("${twilio.to}")
	private String to;

	public String sendMessage(ToDoEntity toDoEntity) throws TwilioRestException {

		params.put("Body", "ToDo task with title : " + toDoEntity.getTitle()
				+ " has been marked as done.");
		params.put("To", to);
		params.put("From", from);

		SmsFactory messageFactory = twilioRestClient.getAccount()
				.getSmsFactory();
		Sms message = messageFactory.create(params);
		LOGGER.fine(message.getSid());
		
		params.clear();
		
		return "SMS with ID : " + message.getSid() + " has been sent.";
	}
	
	public void setTwilioRestClient(TwilioRestClient twilioRestClientNew) {
		twilioRestClient = twilioRestClientNew;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public Map<String, String> getParams() {
		return params;
	}

}
