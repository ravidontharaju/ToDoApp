package com.ravid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ravid.entity.ToDoEntity;
import com.ravid.repository.ToDoRepository;
import com.twilio.sdk.TwilioRestException;

@Component
public class ToDoService {

	@Autowired
	private ToDoRepository repository;

	public ToDoEntity get(String title) {
		ToDoEntity toDoEntity = repository.findByTitle(title);
		return toDoEntity;
	}

	public void delete(String title) {
		repository.delete(repository.findByTitle(title));
	}

	public void save(ToDoEntity toDoEntity) {
		repository.findByTitle(toDoEntity.getTitle());
		
		repository.save(toDoEntity);
	}

	public ToDoEntity update(ToDoEntity toDoEntity) throws TwilioRestException {
		ToDoEntity entity = repository.findByTitle(toDoEntity.getTitle());
		entity.setBody(toDoEntity.getBody());
		entity.setDone(toDoEntity.isDone());
		repository.save(entity);

		if (entity.isDone()) {
			TwilioService.sendMessage(entity);
		}
		return entity;

	}

	public ToDoEntity search(String searchString) {
		// TODO Auto-generated method stub
		return null;
	}

}
