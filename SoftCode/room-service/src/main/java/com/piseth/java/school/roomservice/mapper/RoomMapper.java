package com.piseth.java.school.roomservice.mapper;

import org.springframework.stereotype.Component;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;

@Component
public class RoomMapper {
	
	// DTO -> Entity (Purpose Save) 
	public Room toRoom(RoomDTO roomDTO) {
		
		Room room = new Room();
		room.setName(roomDTO.getName());
		room.setAttributes(roomDTO.getAttributes());		
		return room;
	}
	
	// Entity -> DTO (Purpose show to End user) 
		public RoomDTO toRoomDTO(Room room) {
			
			RoomDTO dto = new RoomDTO();
			dto.setName(room.getName());
			dto.setAttributes(room.getAttributes());		
			return dto;
		}

}
