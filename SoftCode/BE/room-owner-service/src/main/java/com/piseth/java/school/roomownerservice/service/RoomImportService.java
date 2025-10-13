package com.piseth.java.school.roomownerservice.service;

import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.roomownerservice.dto.RoomImportSummary;

import reactor.core.publisher.Mono;

public interface RoomImportService {
	
	Mono<RoomImportSummary> importRooms(FilePart filePart);

}
