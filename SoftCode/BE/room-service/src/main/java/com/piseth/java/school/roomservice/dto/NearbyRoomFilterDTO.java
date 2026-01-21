package com.piseth.java.school.roomservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NearbyRoomFilterDTO {

    @NotNull
    private Double lat;

    @NotNull
    private Double lon;

    @Min(100)
    @Max(50000)
    private Integer radiusMeters = 3000;

    @Min(0)
    private Integer page = 0;

    @Min(1)
    @Max(100)
    private Integer size = 20;

    private RoomFilterDTO otherFilters;
    
    // merged filter:
    
    //private RoomType roomType;
    //private PropertyType propertyType;
    //private Boolean hasWiFi;
}