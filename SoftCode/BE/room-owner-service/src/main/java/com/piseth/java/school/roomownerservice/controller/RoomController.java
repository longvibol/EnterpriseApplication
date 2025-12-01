package com.piseth.java.school.roomownerservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.piseth.java.school.roomownerservice.dto.PageDTO;
import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomFilterDTO;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.dto.RoomUpdateRequest;
import com.piseth.java.school.roomownerservice.service.RoomService;
import com.piseth.java.school.roomownerservice.service.security.CurrentOwnerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
	
	private final RoomService roomService;
	//private final RoomImportService roomImportService;
	
	// call Ownser service 	
	private final CurrentOwnerService currentOwnerService;
	
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<RoomResponse> create(@Valid @RequestBody final RoomCreateRequest req) {
      return currentOwnerService.getCurrentOwnerId()
    		  	.flatMap(ownerId -> roomService.create(req, ownerId));
  }
  
  @PatchMapping("/{id}")
  public Mono<RoomResponse> update(@PathVariable final String id,
                                   @Valid @RequestBody final RoomUpdateRequest req) {
	  return currentOwnerService.getCurrentOwnerId()
  		  	.flatMap(ownerId -> roomService.update(id,req, ownerId));
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable final String id) {
	  return currentOwnerService.getCurrentOwnerId()
  		  	.flatMap(ownerId -> roomService.delete(id, ownerId));
  }
  
  @GetMapping("/{id}")
  public Mono<RoomResponse> getById(@PathVariable final String id) {
	  return currentOwnerService.getCurrentOwnerId()
	  		  	.flatMap(ownerId -> roomService.getById(id, ownerId));
  }
  
  @GetMapping
  public Mono<PageDTO<RoomResponse>> getRoomByFilterPagination(final RoomFilterDTO roomFilterDTO) {

      return currentOwnerService.getCurrentOwnerId()
    		  	.flatMap(ownerId -> roomService.getRoomByFilterPagination(roomFilterDTO, ownerId));
	/*
	@PostMapping
	public Mono<RoomDTO> createRoom(@Valid @RequestBody RoomDTO roomDTO){
		
		return roomService.createRoom(roomDTO);
	}
	
	@GetMapping("/{roomId}")
	@Operation(summary = "Get room by ID", parameters = @Parameter(in = ParameterIn.PATH, name = "roomId"))
	public Mono<RoomDTO> getRoomById(@PathVariable String roomId){
		return roomService.getRoomById(roomId);
	}
	
	@PutMapping("/{roomId}")
	public Mono<RoomDTO> updateRoom(@PathVariable String roomId, @RequestBody RoomDTO roomDTO){
		
		return roomService.updateRoom(roomId, roomDTO);
	}
	
	@DeleteMapping("/{roomId}")
	public Mono<Void> deleteRoom(@PathVariable String roomId){
		
		return roomService.deleteRoom(roomId);
	}
	
	@GetMapping("/search")
	public Flux<RoomDTO> getRoomByFilter(RoomFilterDTO roomFilterDTO){
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
	*/
  }
}