package com.piseth.java.school.roomownerservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomownerservice.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/rooms")
public class RoomController {
		
	private final RoomService roomService;
	//private final RoomImportService roomImportService;
	
	/*
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
	
	@GetMapping("/all")
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
		return roomImportService.importRooms(filePart);
	}
	
	@GetMapping(value = "/room_upload")
	public Mono<RoomImportSummary> uploadRoom(){
		return null;
	}
	*/
}





















