package com.ravid.main.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ravid.main.entity.ToDoEntity;

public interface ToDoRepository extends MongoRepository<ToDoEntity, String> {

	public ToDoEntity findByTitle(String title);
	public List<ToDoEntity> findByTitleContainingOrBodyContaining(String title, String body);
}
