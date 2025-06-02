package com.piseth.java.school.roomservice.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Room {

	@Id
	private String id;
	private String name;
	private Map<String, Object> attributes = new HashMap<>();
	
}
