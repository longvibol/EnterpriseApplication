package com.piseth.java.school.roomservice.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {
	
	/*
	
	@Mock
	private RoomRepository roomRepository; 

	@Mock
	private RoomMapper roomMapper;
	
	@Mock
	private RoomCustomRepository roomCustomRepository;
	
	@InjectMocks
	private RoomServiceImpl roomService;

	@Test
	void createRoom_success() {
		// Given : parameter input to the function that make this function work 
		
		RoomDTO roomDTO = new RoomDTO();
		roomDTO.setName("Luxury"); 
		
		Room room = new Room();
		room.setName("Luxury");
		
		// When : Called this method 
		
		when(roomRepository.save(room)).thenReturn(Mono.just(room));
		when(roomMapper.toRoom(roomDTO)).thenReturn(room);		
		when(roomMapper.toRoomDTO(room)).thenReturn(roomDTO); 
		
		
		// Then : What we expected the result 
		
		StepVerifier.create(roomService.createRoom(roomDTO))
			.expectNext(roomDTO)
			.verifyComplete();
	
	}
	*/
}
