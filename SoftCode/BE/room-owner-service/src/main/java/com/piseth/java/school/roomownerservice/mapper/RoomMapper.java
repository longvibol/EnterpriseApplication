package com.piseth.java.school.roomownerservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.piseth.java.school.roomownerservice.domain.Address;
import com.piseth.java.school.roomownerservice.domain.Room;
import com.piseth.java.school.roomownerservice.dto.AddressDTO;
import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomResponse;
import com.piseth.java.school.roomownerservice.messaging.event.RoomFullPayload;

@Mapper(componentModel = "spring")
public interface RoomMapper {
	
  // Create → Entity
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "availableFrom", ignore = true)
  @Mapping(target = "availableTo", ignore = true)
  @Mapping(target = "extraAttributes", expression = "java(new java.util.HashMap<>())")
  Room toEntity(RoomCreateRequest req);

  /*
  // Update (patch) → Entity
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(@MappingTarget Room target, RoomUpdateRequest req);
*/
  // Address conversions
  Address toAddress(AddressDTO dto);
  AddressDTO toAddressDto(Address address);

  // Entity → Response
  RoomResponse toResponse(Room entity);

  // Entity → Full event payload (for Kafka)
  @Mapping(target = "address", source = "address")
  RoomFullPayload toFullPayload(Room entity);

  @Mapping(target = "geo.latitude", source = "geo.latitude")
  @Mapping(target = "geo.longitude", source = "geo.longitude")
  RoomFullPayload.AddressPayload toAddressPayload(Address address);
  
  
	
}