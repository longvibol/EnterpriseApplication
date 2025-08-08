package com.piseth.java.school.roomservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.PageDTO;
import com.piseth.java.school.roomservice.dto.RoomDTO;
import com.piseth.java.school.roomservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomservice.dto.RoomImportSummary;
import com.piseth.java.school.roomservice.service.RoomImportService;
import com.piseth.java.school.roomservice.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/rooms")
public class RoomController {
		
	private final RoomService roomService;
	private final RoomImportService romImportService;
	
		
	@PostMapping
	@Operation(summary = "Create Room")
	public Mono<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO){
		return roomService.createRoom(roomDTO);		
	}

	@GetMapping("/{roomId}")
	@Operation(summary = "Get room by ID", parameters = @Parameter(in = ParameterIn.PATH,name = "roomId"))
	public Mono<RoomDTO> getRoomById(@PathVariable String roomId){
		return roomService.getRoomById(roomId);
	}
	
	@GetMapping
	@Operation(summary = "Get All Rooms")
    public Flux<Room> getAllRooms() {
        return roomService.getAllRoom();
    }

	
	@PutMapping("/{roomId}")
	@Operation(summary = "Update Room By roomId")
	public Mono<RoomDTO> updateRoom(@PathVariable String roomId,@RequestBody RoomDTO roomDTO){
		return roomService.updateRoom(roomId, roomDTO);
	}
	
	@DeleteMapping("/{roomId}")
	@Operation(summary = "Deleted Room by roomId")
	public Mono<Void> deleteRoom(@PathVariable String roomId){
		return roomService.deleteRoom(roomId);
	}
	
	//Study Purpose only	
	@GetMapping("/search1")
	public Flux<RoomDTO> findRoomByName(@RequestParam String name){
		return roomService.searchRoomByName(name);
	}
	
	// Build Filter controller 	
	@GetMapping("/search")
	public Flux<RoomDTO> getRoomByFilter(RoomFilterDTO roomFilterDTO ){		
		return roomService.getRoomByFilter(roomFilterDTO);		
	}
	
	@GetMapping("/search/pagination")
	public Mono<PageDTO<RoomDTO>> getRoomByFilterPagination(RoomFilterDTO roomFilterDTO){
		return roomService.getRoomByFilterPagination(roomFilterDTO);
	}
	
	
	@GetMapping("/search/pagination2")
	public Mono<ResponseEntity<PageDTO<RoomDTO>>> getRoomByFilterPaginationWithHeader(RoomFilterDTO roomFilterDTO){
		
		return roomService.getRoomByFilterPagination(roomFilterDTO)
					.map(page -> ResponseEntity.ok()
							.header("X-Total-Count", String.valueOf(page.getTotalElements()))
							.body(page)
							);					
	}
	
	@PostMapping(value = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<RoomImportSummary> uploadExcel(@RequestPart("file") FilePart filePart){
		return romImportService.importRooms(filePart);
	}
	
	@GetMapping(value = "/room_upload")
	public Mono<RoomImportSummary> uploadRoom(){
		return null;
	}
	
}





















