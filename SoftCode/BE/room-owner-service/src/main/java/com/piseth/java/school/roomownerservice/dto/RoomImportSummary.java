package com.piseth.java.school.roomownerservice.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomImportSummary {
	private int inserted;
	private int skipped;
	private List<Integer> skippedRow; 
	private Map<Integer, String> reasons;

}
