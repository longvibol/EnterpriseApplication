package com.piseth.java.school.roomownerservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.piseth.java.school.roomownerservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomownerservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String id;
    private String ownerId;

    private String name;
    private String description;

    private Double price;
    private String currencyCode;

    private Integer floor;
    private Double roomSize;
    private RoomType roomType;
    private PropertyType propertyType;

    private AddressDTO address;

    private Boolean hasFan;
    private Boolean hasAirConditioner;
    private Boolean hasParking;
    private Boolean hasPrivateBathroom;
    private Boolean hasBalcony;
    private Boolean hasKitchen;
    private Boolean hasFridge;
    private Boolean hasWashingMachine;
    private Boolean hasTV;
    private Boolean hasWiFi;
    private Boolean hasElevator;

    private Integer maxOccupants;
    private Boolean isPetFriendly;
    private Boolean isSmokingAllowed;
    private Boolean isSharedRoom;
    private GenderPreference genderPreference;

    private Double distanceToCenter;
    private Boolean isUtilityIncluded;
    private Boolean depositRequired;
    private Double depositAmount;
    private Integer minStayMonths;
    private String contactPhone;

    private List<String> photoUrls;
    private String videoUrl;
    private Boolean verifiedListing;

    private RoomStatus status;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    private Map<String, Object> extraAttributes;
}