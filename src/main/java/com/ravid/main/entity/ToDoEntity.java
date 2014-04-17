package com.ravid.main.entity;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@XmlRootElement
@Document(collection = ToDoEntity.COLLECTION_NAME)
public class ToDoEntity {

	public static final transient String COLLECTION_NAME = "ToDo";

	@Id
	private String title;

	private String body;

	private Boolean done;

	public ToDoEntity() {

	}

	public ToDoEntity(String title, String body, Boolean done) {
		this.title = title;
		this.body = body;
		this.done = done;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean isDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	@Override
	public String toString() {
		return new StringBuffer(" Title : ").append(this.title)
				.append("\n Body : ").append(this.body).append("\n Status : ")
				.append(this.done).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ToDoEntity) {

			ToDoEntity entity = (ToDoEntity) obj;
			if (this.title.equals(entity.getTitle())
					&& this.body.equals(entity.getBody())
					&& this.done == entity.isDone()) {
				return true;
			}

		}
		return false;
	}

}
