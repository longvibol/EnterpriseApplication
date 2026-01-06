package com.piseth.java.school.roomownerservice.service.helper;

import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.roomownerservice.dto.RoomParsedRow;

import reactor.core.publisher.Flux;

public interface ExcelRoomParser {

    Flux<RoomParsedRow> parse(FilePart file);
}