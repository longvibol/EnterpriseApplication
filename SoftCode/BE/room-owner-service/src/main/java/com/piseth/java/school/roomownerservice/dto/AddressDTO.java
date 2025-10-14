package com.piseth.java.school.roomownerservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private String provinceCode;
    private String districtCode;
    private String communeCode;
    private String villageCode;

    private String provinceName;
    private String districtName;
    private String communeName;
    private String villageName;

    private String line1;
    private String line2;
    private String postalCode;

    private List<String> nearbyLandmarks;
    private GeoLocationDto geo;
}