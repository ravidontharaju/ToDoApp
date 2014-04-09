package com.ravid.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ravid.main.entity.ToDoEntity;
import com.ravid.main.exception.ToDoException;
import com.ravid.main.repository.ToDoRepository;
import com.twilio.sdk.TwilioRestException;

@Component
public class ToDoService {

	private static final Logger LOGGER = Logger.getLogger(ToDoService.class
			.getSimpleName());

	@Autowired
	private ToDoRepository repository;

	@Autowired
	private TwilioService twilioService;

	@Value("${dev.mode}")
	private boolean devMode;

	public ToDoEntity get(String title) {
		ToDoEntity toDoEntity = repository.findByTitle(title);
		return toDoEntity;
	}

	public void delete(String title) {
		repository.delete(repository.findByTitle(title));
	}

	public void save(ToDoEntity toDoEntity) throws ToDoException {
		if (repository.findByTitle(toDoEntity.getTitle()) == null) {
			repository.save(toDoEntity);
		} else {
			throw new ToDoException("ToDo task already exists");
		}
	}

	public ToDoEntity update(ToDoEntity toDoEntity) throws TwilioRestException {
		ToDoEntity entity = repository.findByTitle(toDoEntity.getTitle());
		if (entity.equals(toDoEntity)) {
			LOGGER.info("No changes detected, ignoring update. Task is marked as "
					+ taskStatus(entity.isDone()));
			return entity;
		}

		entity.setBody(toDoEntity.getBody());
		entity.setDone(toDoEntity.isDone());
		repository.save(entity);

		if (entity.isDone()) {
			LOGGER.info("Entity updated, sending a message");
			if (!devMode) {
				twilioService.sendMessage(entity);
			}
		}
		return entity;
	}

	public List<ToDoEntity> search(String searchString) {
		List<ToDoEntity> entities = repository
				.findByTitleContainingOrBodyContaining(searchString,
						searchString);

		if (entities == null || entities.isEmpty()) {
			return new ArrayList<>();
		}
		return entities;
	}

	public List<ToDoEntity> getAll() {
		List<ToDoEntity> entities = repository.findAll();

		if (entities == null || entities.isEmpty()) {
			return new ArrayList<>();
		}
		return entities;
	}

	public void setToDoRepository(ToDoRepository repository) {
		this.repository = repository;
	}

	public void setTwilioService(TwilioService twilioService) {
		this.twilioService = twilioService;
	}
	
	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	private String taskStatus(boolean done) {
		return done ? " DONE." : " NOT DONE.";
	}

}
