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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.domain.SkippedRoomDocument;
import com.piseth.java.school.roomservice.dto.RoomImportSummary;
import com.piseth.java.school.roomservice.repository.RoomRepository;
import com.piseth.java.school.roomservice.repository.SkippedRoomDocumentRepository;
import com.piseth.java.school.roomservice.service.RoomImportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomImportServiceImple implements RoomImportService{
	
	private final SkippedRoomDocumentRepository skippedRoomDocumentRepository;
	private final RoomRepository roomRepository;

	@Override
	public Mono<RoomImportSummary> importRooms(FilePart filePart) {
		
		return filePart.content() // Flux<DataBuffer> 
			.map(dataBuffer -> {
				
				byte[] bytes = new byte[dataBuffer.readableByteCount()];
				dataBuffer.read(bytes); // we read data from data buffer and store in byte[] 
				
				// we need to have one operation to protect memory lead (clean up memory)
				
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
					continue; // after we add then we continue to next row 
				}		
				
				// get cell 	
				String name = getString(row.getCell(0)); // RoomA 
				Double price = getDouble(row.getCell(1)); // 100$
				Double floorValue = getDouble(row.getCell(2));
				Integer floor = (floorValue != null) ? floorValue.intValue() : null; // Floor-integer 
				String type = getString(row.getCell(3)); // type 
				
				// add to row data 
				Map<String, Object> rowData = new HashMap<>();
				rowData.put("name", name);
				rowData.put("price", price);
				rowData.put("floor", floor);
				rowData.put("type", type);
				
				// validaton name, price, floor and type it valid or not before we insert to database 
				// want to know if they forget to put name we tell, you forget name 
				
				String reason = null;
				if(!StringUtils.hasText(name)) {
					reason = "Missing room name";
				} 
				else if (price == null) {
					reason = "Missing or invalid price";
				}
				else if (floor == null) {
					reason = "Missing or invalid floor";
				}
				else if (type == null) {
					reason = "Missing or invalid type";
				}
				
				// have problem or error 
				if(reason != null) {
					skippedRow.add(displayRow);
					reasons.put(displayRow, reason);
					skippedRoomDocuments.add(buildSkippedRoomDocument(displayRow, rowData, reason, bathId));
					continue;  
				}				
				// Final valid data that we can save 
				
				Room room = new Room();
				room.setName(name);
				//room.setAttributes(rowData);
	
				// We need to wait until finished checking all the row 
				log.debug("Prepared to save room : {}",room);
				validRoom.add(room);			
				
				// End loop 
			}
			 log.info("Valid rooms to save: {}", validRoom.size());
			 
			 return skippedRoomDocumentRepository.saveAll(skippedRoomDocuments)
			 	.thenMany(roomRepository.saveAll(validRoom)
			 			.doOnNext(r -> log.info("Save room: {}",r))
			 			.doOnError(err -> log.error("Error saving room: {}", err.getMessage(), err))
			 )
			 .collectList()	
			 .map(save -> new RoomImportSummary(save.size(),skippedRoomDocuments.size(),skippedRow,reasons));		 
			
		} catch (IOException e) {
			log.error("Fail to parse Excel", e);
			return Mono.error(new RuntimeException("Fail to read Excel file", e));
		}
		
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
	
	private String getString(Cell cell) {
		return cell == null ? null : cell.getStringCellValue();
	}
	
	private Double getDouble(Cell cell) {
		// if the input string we need to do try catch 		
		try {
			return cell ==null ? null : cell.getNumericCellValue();
		} catch (Exception e) {
			return null;
		}		
	}
	
	public Mono<RoomImportSummary> updateRoomSuccess(FilePart filePart) {
		
		
		return null;
	}

}
