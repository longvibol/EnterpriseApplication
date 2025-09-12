package com.piseth.java.school.roomservice.domain;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collation = "skipped_rooms")
public class SkippedRoomDocument {
	
	@Id
	private String id;
	
	private int rowNumber; // wich row it error 
	
	private Map<String, Object> rowData; // we capture 
	
	private String reason; // what is the reason 
	private LocalDateTime uploadDate;
	private String uploadBatchId; // number of batch that we upload

}
