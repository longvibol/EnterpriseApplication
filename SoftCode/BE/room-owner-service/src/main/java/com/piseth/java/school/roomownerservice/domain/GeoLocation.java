package com.piseth.java.school.roomownerservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {
    private Double latitude;
    private Double longitude;
}