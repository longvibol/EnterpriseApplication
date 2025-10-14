package com.piseth.java.school.roomownerservice.dto;

import java.util.List;

import com.piseth.java.school.roomownerservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomownerservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomCreateRequest {

    @NotBlank
    private String ownerId;

    @NotBlank
    private String name;

    private String description;

    @NotNull @Positive
    private Double price;

    @NotBlank
    private String currencyCode;

    @Min(0)
    private Integer floor;

    @Positive
    private Double roomSize;

    @NotNull
    private RoomType roomType;

    @NotNull
    private PropertyType propertyType;

    @NotNull
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

    @Positive
    private Integer maxOccupants;
    private Boolean isPetFriendly;
    private Boolean isSmokingAllowed;
    private Boolean isSharedRoom;
    private GenderPreference genderPreference;

    private Double distanceToCenter;
    private List<String> nearbyLandmarks;
    private Boolean isUtilityIncluded;
    private Boolean depositRequired;
    @Positive
    private Double depositAmount;
    @Positive
    private Integer minStayMonths;
    private String contactPhone;

    private List<String> photoUrls;
    private String videoUrl;
    private Boolean verifiedListing;

    private RoomStatus status; // optional; default AVAILABLE in service
}