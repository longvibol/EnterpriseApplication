package com.piseth.java.school.roomservice.domain;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    private String country;
    private String city;
    private String district;
    private String street;
    private String fullAddress;
    private GeoJsonPoint coordinates; // GeoJSON format for geospatial queries
}