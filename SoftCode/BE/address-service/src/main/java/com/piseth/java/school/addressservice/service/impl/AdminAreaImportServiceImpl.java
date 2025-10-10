package com.piseth.java.school.addressservice.service.impl;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.piseth.java.school.addressservice.domain.AdminArea;
import com.piseth.java.school.addressservice.domain.enumeration.Outcome;
import com.piseth.java.school.addressservice.dto.AdminAreaCreateRequest;
import com.piseth.java.school.addressservice.dto.ParseRow;
import com.piseth.java.school.addressservice.dto.RowError;
import com.piseth.java.school.addressservice.dto.RowResult;
import com.piseth.java.school.addressservice.dto.UploadSummary;
import com.piseth.java.school.addressservice.mapper.AdminAreaMapper;
import com.piseth.java.school.addressservice.mapper.ParsedRowMapper;
import com.piseth.java.school.addressservice.mapper.UploadSummaryMapper;
import com.piseth.java.school.addressservice.service.AdminAreaImportService;
import com.piseth.java.school.addressservice.service.AdminAreaService;
import com.piseth.java.school.addressservice.service.ExcelAdminAreaParser;
import com.piseth.java.school.addressservice.service.helper.ImportAccumulator;
import com.piseth.java.school.addressservice.service.helper.RowErrorClassifier;
import com.piseth.java.school.addressservice.validator.AdminAreaValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class AdminAreaImportServiceImpl implements AdminAreaImportService{
	
	private final ExcelAdminAreaParser parser;
	private final ParsedRowMapper parsedRowMapper;
	private final AdminAreaMapper adminAreaMapper;
	private final AdminAreaValidator validator;
	private final AdminAreaService adminAreaService;
	private final RowErrorClassifier rowErrorClassifier;
	private final UploadSummaryMapper uploadSummaryMapper;

	@Override
	public Mono<UploadSummary> importExcel(FilePart file, boolean dryRun) {
		
		return parser.parse(file)
			.sort(ParseRow.BY_DEPTH)
			.concatMap(row -> handleRow(row, dryRun)) // we want it to insert provice firs 
//			.reduce(new ImportAccumulator(), ImportAccumulator::accumulator)
			.reduce(new ImportAccumulator(), ImportAccumulator::accumulate)
			.map(uploadSummaryMapper::toUploadSummary);
	}
	
	private Mono<RowResult> handleRow(final ParseRow row, boolean dryRun){
		
		AdminAreaCreateRequest createRequest = parsedRowMapper.toCreateRequest(row);
		return validate(createRequest)
			.then(maybeCreate(createRequest, dryRun))
		// within this line we get true or false but we want RowResult
			.map(ok -> RowResult.inserted())				
				// in this RowResult we want to know if it have something wrong 		
			.onErrorResume(ex ->{
				final Outcome outcome = rowErrorClassifier.classify(ex);
				final String msg = rowErrorClassifier.safeMessage(ex);
				
				RowError error = new RowError(row.lineNumber(),row.code(), msg);
				
				return Mono.just(RowResult.error(outcome, error));
				// need outcome and error we need to cut from ex 
			});		
	}
	
	// check dryRun
	private Mono<Boolean> maybeCreate(final AdminAreaCreateRequest req,boolean dryRun){
		if(dryRun) {
			return Mono.just(Boolean.TRUE);
		}
		
		// save to database but we retrun only the boolean 
		return adminAreaService.create(req).thenReturn(Boolean.TRUE);
	}
	
	// validate 
	// we have our validat AdminAreaValidator so we need to convert from AdminAreaCreateRequest to AdminAreaValidator
	
	private Mono<Void> validate(final AdminAreaCreateRequest req){
		return Mono.fromRunnable(()->{
			AdminArea adminArea = adminAreaMapper.toEntity(req);
			validator.validate(adminArea);
		});
	}

}
