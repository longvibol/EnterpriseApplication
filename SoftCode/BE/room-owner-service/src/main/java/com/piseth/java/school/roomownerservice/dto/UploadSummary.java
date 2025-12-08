package com.piseth.java.school.roomownerservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadSummary {

	private int total;
    private int inserted;
    private int validationErrors;
    private int duplicateErrors;
    private int otherErrors;

    private List<RowError> errors;
}