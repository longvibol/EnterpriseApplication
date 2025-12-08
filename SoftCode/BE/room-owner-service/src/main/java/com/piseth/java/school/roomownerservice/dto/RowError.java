package com.piseth.java.school.roomownerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowError {

    private int lineNumber;
    private String key;
    private String message;
}