package com.piseth.java.school.roomownerservice.dto;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RowResult {

    private Outcome outcome;
    private RowError error;

    public static RowResult inserted() {

        return new RowResult(Outcome.INSERTED, null);
    }

    public static RowResult error(final Outcome outcome, final RowError error) {

        return new RowResult(outcome, error);
    }
}