package com.piseth.java.school.addressservice.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.piseth.java.school.addressservice.domain.enumeration.AdminLevel;
import com.piseth.java.school.addressservice.dto.ParseRow;
import com.piseth.java.school.addressservice.service.ExcelAdminAreaParser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/*
 * - we read the whole file content as bytes and then let Apache POI parse it 
 * - we run blocking POI work on (boundedElastic)
 * - we normalize header to lowercase
 * - we use DataFormatter to get cell values as displayed text 
 * 

 */

@Component
public class ExcelAdminAreaParserImpl implements ExcelAdminAreaParser{
	
	//Header setup can not duplicate 
	private static final Set<String> REQUIRED_HAEDERS = Set.of(
			"code","level","parentcode","namekh","nameen"
			);

	@Override
	public Flux<ParseRow> parse(FilePart file) {
		return DataBufferUtils.join(file.content())
			.flatMapMany(buf ->{
				try {
					final byte[] bytes = toBytes(buf);
					return parseBytes(bytes);
				} finally {
					DataBufferUtils.release(buf);
				}
			});
	}
	
	// we read the whole file content as bytes and then let Apache POI parse it
	private byte[] toBytes(DataBuffer buf) {
		final byte[] bytes = new byte[buf.readableByteCount()];
		buf.read(bytes);
		return bytes;
	}
	
	// 2 convert from byte to Flux<ParseRow>
	private Flux<ParseRow> parseBytes(final byte[] bytes){
		 return Mono.fromCallable(() -> readRows(bytes))
		 	.subscribeOn(Schedulers.boundedElastic())
		 	.flatMapMany(Flux::fromIterable);
	}
	
	
	// 1 when read write file it is blocking (do first to List then convert to Flux) 
	private List<ParseRow> readRows(final byte[] bytes) throws Exception{
		final List<ParseRow> out = new ArrayList<>();
		
		try(Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(bytes))){
			// catch sheet in the workbook
			final Sheet sheet = wb.getNumberOfSheets()>0 ? wb.getSheetAt(0) : null;
			if(sheet == null) {
				throw new IllegalArgumentException("Excel file has no sheet!");
			}
			
			// create format to the data 
			final DataFormatter fmt = new DataFormatter(Locale.ROOT);
			
			//we get the header now
			final Row header = sheet.getRow(0); 
			
			if(header == null) {
				throw new IllegalArgumentException("Missing header row");
			}
			
			// we want to get the header by the code name : ex: code =1; level =2 is is Map<S,I>
			
			final Map<String, Integer> idx = headerIndex(header, fmt);
			
			//check contain header 
			if(!idx.keySet().containsAll(REQUIRED_HAEDERS)) {
				throw new IllegalArgumentException("Header must include: code, level,parentcode,namekh,nameen");
			}
			
			// loop row 
			final int last = sheet.getLastRowNum();
			for(int r =1 ; r <= last; r++) {
				final Row row = sheet.getRow(r);
				if(row == null) {
					continue;
				}
				final String code = cell(row, idx.get("code"), fmt);
				final String level = cell(row, idx.get("level"), fmt);
				final String parentcode = cell(row, idx.get("parentcode"), fmt);
				final String nameKh = cell(row, idx.get("namekh"), fmt);
				final String nameEn = cell(row, idx.get("nameen"), fmt);
				
				// if the cell Empty we no need to do 
				
				if(isBlank(code) && 
						isBlank(level) && 
						isBlank(parentcode) && 
						isBlank(nameKh) && 
						isBlank(nameEn)) {
					continue;
				}
				
				// validate level it need to be the ask we set :PROVINCE, DISTRICT... 		
				final AdminLevel adminLevel = parseLevel(level);
				out.add(new ParseRow(r+1, code, adminLevel, parentcode, nameKh, nameEn));				
				// r + 1 = line number start from 2				
			}
		}
		
		return out;
	}
	
	// check admin level 
	private AdminLevel parseLevel(final String raw) {
		if(raw == null) {
			return null;
		}
		final String u = raw.trim().toUpperCase();
		for(AdminLevel lvl : AdminLevel.values()) {
			if(lvl.name().equals(u)) {
				return lvl;
			}
		}
		
		throw new IllegalArgumentException("Unknow level");
	}
	
	// check the cell data have value or not 	
	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	// function to catch cell	
	private String cell(final Row row, final Integer col, final DataFormatter fmt) {
		if(row == null || col == null) {
			return null;
		}
		
		final Cell cell = row.getCell(col);
		if(cell == null) {
			return null;
		}
		final String v = fmt.formatCellValue(cell);
		
		// not value 
		if(v == null) {
			return null;
		}
		final String t = v.trim();
		
		//have space 
		if(t.isEmpty()) {
			return null;
		}
		
		return t;
	}
	
	// get header index
	private Map<String, Integer> headerIndex(final Row header, final DataFormatter fmt){
		// create Map to store the data 
		final Map<String, Integer> idx = new HashMap<>();
		for(int c=0; c< header.getLastCellNum(); c++) {
			
			// we get header : code, level -> we need to get below header 
			final Cell cell = header.getCell(c);
			final String raw = cell != null ? fmt.formatCellValue(cell) : null;
			final String key = raw != null ? raw.trim().toLowerCase(Locale.ROOT) : "";
			if(!key.isBlank()) {
				idx.put(key, c);
			}
		}
		
		return idx;
		
	}
	
	
	

}















