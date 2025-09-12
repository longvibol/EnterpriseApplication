package com.piseth.java.school.roomservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;

@Mapper(componentModel = "spring")
public interface RoomMapper {

	@Mapping(target = "id", ignore=true)
	Room toRoom(RoomDTO roomDTO);
	
	RoomDTO toRoomDTO(Room room);
	
	@Mapping(target = "id", ignore=true) // to stop create new object 
	void updateRoomFromDTO(RoomDTO roomDTO, @MappingTarget Room room);

}
