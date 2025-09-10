package com.piseth.java.school.roomservice.service;

import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.roomservice.dto.RoomImportSummary;

import reactor.core.publisher.Mono;

public interface RoomImportService {
	
	Mono<RoomImportSummary> importRooms(FilePart filePart);

}
