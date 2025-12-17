package com.piseth.java.school.roomownerservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.piseth.java.school.roomownerservice.dto.RoomCreateRequest;
import com.piseth.java.school.roomownerservice.dto.RoomParsedRow;

@Mapper(componentModel = "spring")
public interface RoomParsedRowMapper {

    @Mapping(target = "address.provinceCode", source = "provinceCode")
    @Mapping(target = "address.districtCode", source = "districtCode")
    @Mapping(target = "address.communeCode", source = "communeCode")
    @Mapping(target = "address.villageCode", source = "villageCode")
    @Mapping(target = "address.line1", source = "addressLine1")
    @Mapping(target = "address.line2", source = "addressLine2")
    @Mapping(target = "address.postalCode", source = "postalCode")
    @Mapping(target = "address.geo.latitude", source = "latitude")
    @Mapping(target = "address.geo.longitude", source = "longitude")
    RoomCreateRequest toCreateRequest(RoomParsedRow row);
}