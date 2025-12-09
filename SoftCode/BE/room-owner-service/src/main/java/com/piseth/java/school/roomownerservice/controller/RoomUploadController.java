package com.piseth.java.school.roomownerservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomownerservice.dto.UploadSummary;
import com.piseth.java.school.roomownerservice.service.RoomImportService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomUploadController {

    private final RoomImportService roomImportService;

    @PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<UploadSummary> uploadExcel(@RequestPart("file") final FilePart file,
                                           @RequestParam(defaultValue = "false") final boolean dryRun) {

        return roomImportService.importExcel(file, dryRun);
    }
}