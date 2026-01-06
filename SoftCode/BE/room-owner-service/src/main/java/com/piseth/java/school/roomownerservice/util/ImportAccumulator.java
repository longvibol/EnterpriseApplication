package com.piseth.java.school.roomownerservice.util;

import java.util.ArrayList;
import java.util.List;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;
import com.piseth.java.school.roomownerservice.dto.RowError;
import com.piseth.java.school.roomownerservice.dto.RowResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImportAccumulator {

    private int total;
    private int inserted;
    private int validationErrors;
    private int duplicateErrors;
    private int otherErrors;

    private List<RowError> errors = new ArrayList<>();

    public ImportAccumulator accumulate(final RowResult result) {

        if (result == null) {
            return this;
        }

        this.total++;

        if (result.getOutcome() == Outcome.INSERTED) {
            this.inserted++;
        } else {
            if (result.getOutcome() == Outcome.VALIDATION) {
                this.validationErrors++;
            } else if (result.getOutcome() == Outcome.DUPLICATE) {
                this.duplicateErrors++;
            } else {
                this.otherErrors++;
            }
            if (result.getError() != null) {
                this.errors.add(result.getError());
            }
        }

        return this;
    }
}