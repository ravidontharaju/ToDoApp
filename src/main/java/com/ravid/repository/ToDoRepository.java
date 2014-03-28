package com.ravid.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ravid.entity.ToDoEntity;

public interface ToDoRepository extends MongoRepository<ToDoEntity, String> {

	public ToDoEntity findByTitle(String title);

}
