package com.piseth.java.school.roomservice.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomDTO {
	
	@NotBlank(message = "Room name is required")
	@Size(max = 100, message = "Room name must be at most 100 characters")
	private String name;
	
	private Map<String, Object> attributes;

}
