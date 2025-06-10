package com.piseth.java.school.roomservice.mapper;

import org.mapstruct.Mapper;

import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.dto.RoomDTO;

@Mapper(componentModel = "spring")
public interface RoomMapper {

	Room toRoom(RoomDTO roomDTO);
	RoomDTO toRoomDTO(Room room);

}
