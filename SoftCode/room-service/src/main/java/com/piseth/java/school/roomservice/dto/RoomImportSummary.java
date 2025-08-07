package com.piseth.java.school.roomservice.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomImportSummary {

	private int inserted; // how many recode that we insert to the database 
	private int skipped; // how many fail recode can not upload 
	private List<Integer> skippedRow; // what row we skip 
	private Map<Integer, String> reasons; // witch row and reason 
	
}
