package com.piseth.java.school.roomownerservice.service.impl;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;
import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomParsedRow;
import com.piseth.java.school.roomownerservice.dto.RowError;
import com.piseth.java.school.roomownerservice.dto.RowResult;
import com.piseth.java.school.roomownerservice.dto.UploadSummary;
import com.piseth.java.school.roomownerservice.mapper.RoomParsedRowMapper;
import com.piseth.java.school.roomownerservice.mapper.UploadSummaryMapper;
import com.piseth.java.school.roomownerservice.service.RoomImportService;
import com.piseth.java.school.roomownerservice.service.RoomService;
import com.piseth.java.school.roomownerservice.service.helper.ExcelRoomParser;
import com.piseth.java.school.roomownerservice.service.security.CurrentOwnerService;
import com.piseth.java.school.roomownerservice.util.ImportAccumulator;
import com.piseth.java.school.roomownerservice.util.RowErrorClassifier;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomImportServiceImpl implements RoomImportService {
	
	private final ExcelRoomParser parser;
    private final RoomParsedRowMapper parsedRowMapper;
    private final RoomService roomService;

    private final RowErrorClassifier rowErrorClassifier;   
    private final UploadSummaryMapper uploadSummaryMapper; 
    private final CurrentOwnerService currentOwnerService; 

    @Override
    public Mono<UploadSummary> importExcel(final FilePart file, final boolean dryRun) {

        return parser.parse(file)
            .concatMap(row -> handleRow(row, dryRun))
            .reduce(new ImportAccumulator(), ImportAccumulator::accumulate)
            .map(uploadSummaryMapper::toUploadSummary);
    }

    private Mono<RowResult> handleRow(final RoomParsedRow row, final boolean dryRun) {

        final RoomCreateRequest createRequest = parsedRowMapper.toCreateRequest(row);

        return validate(createRequest)
            .then(maybeCreate(createRequest, dryRun))
            .map(ok -> RowResult.inserted())
            .onErrorResume(ex -> {
                final Outcome outcome = rowErrorClassifier.classify(ex);
                final String msg = rowErrorClassifier.safeMessage(ex);
                final RowError error = new RowError(row.lineNumber(), row.name(), msg);
                return Mono.just(RowResult.error(outcome, error));
            });
    }

    private Mono<Boolean> maybeCreate(final RoomCreateRequest req, final boolean dryRun) {

        if (dryRun) {
            // validate only – no DB, no outbox events
            return Mono.just(Boolean.TRUE);
        }

        // Get ownerId from context, not Excel
        return currentOwnerService.getCurrentOwnerId()
            .flatMap(ownerId -> roomService.create(req, ownerId))
            .thenReturn(Boolean.TRUE);
    }

    private Mono<Void> validate(final RoomCreateRequest req) {

        return Mono.fromRunnable(() -> {
            if (req == null) {
                throw new ValidationException("RoomCreateRequest is required");
            }

            if (req.getName() == null || req.getName().isBlank()) {
                throw new ValidationException("name is required");
            }

            if (req.getPrice() == null || req.getPrice() <= 0) {
                throw new ValidationException("price must be > 0");
            }

            if (req.getCurrencyCode() == null || req.getCurrencyCode().isBlank()) {
                throw new ValidationException("currencyCode is required");
            }

            if (req.getRoomType() == null) {
                throw new ValidationException("roomType is required");
            }

            if (req.getPropertyType() == null) {
                throw new ValidationException("propertyType is required");
            }

            if (req.getAddress() == null) {
                throw new ValidationException("address is required");
            }
        });
    }

}