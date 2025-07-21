package com.piseth.java.school.roomservice.service.imple;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piseth.java.school.roomservice.mapper.RoomMapper;
import com.piseth.java.school.roomservice.repository.RoomCustomRepository;
import com.piseth.java.school.roomservice.repository.RoomRepository;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {
	
	@Mock
	private RoomRepository roomRepository; 

	@Mock
	private RoomMapper roomMapper;
	
	@Mock
	private RoomCustomRepository roomCustomRepository;

}
