package com.piseth.java.school.roomownerservice.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.piseth.java.school.roomownerservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomownerservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomType;

import lombok.Data;

@Data
public class RoomDTO {
    private String id;

    private String ownerId; // Reference to Owner User ID

    private String name;
    private String description;

    private Double price;            // Price per month
    private String currencyCode;     // e.g. "USD", "KHR"

    private Integer floor;
    private Double roomSize;         // Square meters
    private RoomType roomType;       // e.g., SINGLE, DOUBLE, STUDIO
    private PropertyType propertyType; // e.g., APARTMENT, HOUSE

    // ----------- AdminArea Linkage -------------
    private AddressDTO address;

    // ----------- Amenities ---------------------
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

    // ----------- Room Rules --------------------
    private Integer maxOccupants;
    private Boolean isPetFriendly;
    private Boolean isSmokingAllowed;
    private Boolean isSharedRoom;
    private GenderPreference genderPreference;

    // ----------- Additional Info ---------------
    private Double distanceToCenter;        // km
    private Boolean isUtilityIncluded;      // Rent includes electricity/water
    private Boolean depositRequired;
    private Double depositAmount;           // Optional
    private Integer minStayMonths;
    private String contactPhone;            // Visible to visitors

    // ----------- Media -------------------------
    private List<String> photoUrls;
    private String videoUrl;
    private Boolean verifiedListing;

    // ----------- Availability ------------------
    private RoomStatus status;              // AVAILABLE, RENTED, HIDDEN
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    // ----------- Flexible Extension ------------
    private Map<String, Object> extraAttributes = new HashMap<>();

}