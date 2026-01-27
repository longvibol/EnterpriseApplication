package com.piseth.java.school.roomservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.piseth.java.school.roomservice.domain.Address;
import com.piseth.java.school.roomservice.domain.GeoLocation;
import com.piseth.java.school.roomservice.domain.Room;
import com.piseth.java.school.roomservice.message.event.RoomFullPayload;

@Mapper(componentModel = "spring")
public interface RoomProjectionMapper {

	@Mapping(target = "id", source = "id")
    @Mapping(target = "lastEventAt", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "geoPoint", source = ".", qualifiedByName = "toGeoPoint")
    Room toProjection(RoomFullPayload src);

    Address toAddress(RoomFullPayload.AddressPayload src);

    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    GeoLocation toGeo(RoomFullPayload.AddressPayload.GeoPayload src);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "geoPoint", source = ".", qualifiedByName = "toGeoPoint")
    void merge(@MappingTarget Room target, RoomFullPayload src);
    
    @Named("toGeoPoint")
    default GeoJsonPoint toGeoPoint(RoomFullPayload src) {
        if (src == null || src.getAddress() == null || src.getAddress().getGeo() == null) {
            return null;

        }
        Double lat = src.getAddress().getGeo().getLatitude();
        Double lon = src.getAddress().getGeo().getLongitude();
        if (lat == null || lon == null) {
            return null;
        }
        // GeoJSON order: [lon, lat]
        return new GeoJsonPoint(lon, lat);

    }
}