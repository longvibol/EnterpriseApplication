package com.piseth.java.school.addressservice.service;

import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.addressservice.dto.ParseRow;

import reactor.core.publisher.Flux;

public interface ExcelAdminAreaParser {
	
	
	Flux<ParseRow> parse(FilePart file);

}
