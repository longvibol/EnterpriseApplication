package com.piseth.java.school.roomservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.piseth.java.school.roomservice.domain.Address;
import com.piseth.java.school.roomservice.domain.GeoLocation;
import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.message.event.RoomFullPayload;

@Mapper(componentModel = "spring")
public interface RoomProjectionMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastEventAt", ignore = true)
    @Mapping(target = "deleted", constant = "false") // assign default value deleted to false : we must use constant 
    Room toProjection(RoomFullPayload src);

    Address toAddress(RoomFullPayload.AddressPayload src);

    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    GeoLocation toGeo(RoomFullPayload.AddressPayload.GeoPayload src);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(@MappingTarget Room target, RoomFullPayload src);
}