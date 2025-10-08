package com.piseth.java.school.addressservice.service.impl;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.ParseRow;
import com.piseth.java.school.addressservice.dto.RowResult;
import com.piseth.java.school.addressservice.dto.UploadSummary;
import com.piseth.java.school.addressservice.mapper.ParsedRowMapper;
import com.piseth.java.school.addressservice.service.AdminAreaImportService;
import com.piseth.java.school.addressservice.service.ExcelAdminAreaParser;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class AdminAreaImportServiceImpl implements AdminAreaImportService{
	
	private final ExcelAdminAreaParser parser;
	private final ParsedRowMapper parsedRowMapper;

	@Override
	public Mono<UploadSummary> importExcel(FilePart file, boolean dryRun) {
		parser.parse(file)
			.sort(ParseRow.BY_DEPTH);
		
		return null;
	}
	
	private Mono<RowResult> handleRow(final ParseRow row, boolean dryRun){
		
		AdminAreaCreateRequest createRequest = parsedRowMapper.toCreateRequest(row);
		return null;
	}

}
