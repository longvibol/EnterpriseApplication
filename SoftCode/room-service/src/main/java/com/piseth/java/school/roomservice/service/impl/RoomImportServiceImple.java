package com.piseth.java.school.roomservice.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.domain.SkippedRoomDocument;
import com.piseth.java.school.roomservice.dto.RoomImportSummary;
import com.piseth.java.school.roomservice.service.RoomImportService;

import reactor.core.publisher.Mono;

public class RoomImportServiceImple implements RoomImportService{

	@Override
	public Mono<RoomImportSummary> importRooms(FilePart filePart) {
		
		return filePart.content() // Flux<DataBuffer> 
			.map(dataBuffer -> {
				
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes); // we read data from databuffer and store in byte[] 
				
				// we need to have one operation to protech memory lead (clean up memory)
				
				DataBufferUtils.release(dataBuffer);
				
				return new ByteArrayInputStream(bytes);				
				// after return we get the byte array input stream 
			})
			.next() // when we get one buffer we want to save to our database ==> create another to save 			
			.flatMap(inputStream -> parseAndSaveRooms(inputStream)); // we convert from flux to mono that why we use flatmap
		
		
	}
	
	private Mono<RoomImportSummary> parseAndSaveRooms(ByteArrayInputStream inputStream){
//		Workbook workbook = new XSSFWorkbook(null); 
		 String bathId = UUID.randomUUID().toString();
		 
		 try (Workbook workbook = new XSSFWorkbook(inputStream)) {
			 Sheet sheet = workbook.getSheetAt(0);
			 
			 // we want to return our roomSummary 
			 // save have two condition : good save to room and not good save to Skip room 
			List<Room> validRoom = new ArrayList<>();
			List<Integer> skippedRow = new ArrayList<>();
			Map<Integer, String> reasons = new HashMap<>();
			
			// error happend we keep in one list 			
			List<SkippedRoomDocument> skippedRoomDocuments = new ArrayList<>();
			
			for (int i =1 ; i<= sheet.getLastRowNum(); i++) {
				// we want to loop each row and check if it have error or not 
				// bad => save in One list ; good => save in one list
				
				Row row = sheet.getRow(i);
				int displayRow = i+1; // make the user see start from 1
				
				// case empty row 
				
				if(row ==null) {
					skippedRow.add(displayRow);
					reasons.put(displayRow, "Empty Row");
					skippedRoomDocuments.add(buildSkippedRoomDocument(displayRow, Collections.EMPTY_MAP, "Empty Row", bathId));
				}					
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private SkippedRoomDocument buildSkippedRoomDocument(int rowNumber, 
			Map<String, Object> rowData, String reason, String uploadBatchId) {
		
		// create object with builder		
		return SkippedRoomDocument.builder()
			.rowNumber(rowNumber)
			.rowData(rowData)
			.reason(reason)
			.uploadBatchId(uploadBatchId)
			.uploadDate(LocalDateTime.now())
			.build();
	}

}
