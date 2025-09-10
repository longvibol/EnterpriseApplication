package com.piseth.java.school.roomservice.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.piseth.java.school.roomservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomservice.domain.enumeration.RoomType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Room {
	
	@Id
	private String id;
	private String name;
	
	private Double price;                  // price per month
    private Integer floor;
    private Double roomSize;              // square meters

    private Location location;

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

    private RoomType roomType;
    private PropertyType propertyType;

    private Double distanceToCenter;        // optional
    private List<String> nearbyLandmarks;   // ["university", "mall"]

    private Boolean isUtilityIncluded; //100
    private Boolean depositRequired;
    private Integer minStayMonths;

    private Boolean hasPhotos;
    private Integer photoCount;
    private Boolean hasVideoTour;

    private Boolean verifiedListing;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    private Map<String, Object> extraAttributes = new HashMap<>();

}
