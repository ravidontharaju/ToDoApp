package com.ravid.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.ravid.entity.ToDoEntity;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;

public class TwilioService {

	private static final Logger LOGGER = Logger.getLogger(TwilioService.class.getName());
	private static final String ACCOUNT_SID = "ACa1ee2c95a9ab87cba8b30437492a245a";
	private static final String AUTH_TOKEN = "ebeadfe7e671c3630cbbe16e25b0ef5b";
	private static TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

	public static void sendMessage(ToDoEntity toDoEntity) throws TwilioRestException {

		Map<String, String> params = new HashMap<String, String>();
		params.put("Body", "ToDo task with title : " + toDoEntity.getTitle() + " has been marked as done.");
		params.put("To", "+17163613680");
		params.put("From", "+19728431617");

		SmsFactory messageFactory = client.getAccount().getSmsFactory();
		Sms message = messageFactory.create(params);
		LOGGER.fine(message.getSid());
	}

}
