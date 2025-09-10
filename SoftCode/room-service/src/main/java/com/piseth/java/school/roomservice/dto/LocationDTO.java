package com.piseth.java.school.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private String country;
    private String city;
    private String district;
    private String street;
    private String fullAddress;
    //private GeoJsonPoint coordinates; // GeoJSON format for geospatial queries
}