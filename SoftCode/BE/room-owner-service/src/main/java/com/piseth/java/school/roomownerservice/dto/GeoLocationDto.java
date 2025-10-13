package com.piseth.java.school.roomownerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationDto {
    private Double latitude;
    private Double longitude;
}