package com.piseth.java.school.roomownerservice.dto;

import com.piseth.java.school.roomownerservice.domain.enumeration.GenderPreference;
import com.piseth.java.school.roomownerservice.domain.enumeration.PropertyType;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomStatus;
import com.piseth.java.school.roomownerservice.domain.enumeration.RoomType;

public record RoomParsedRow(
        int lineNumber,
        String ownerId,
        String name,
        String description,
        Double price,
        String currencyCode,
        Integer floor,
        Double roomSize,
        RoomType roomType,
        PropertyType propertyType,
        String provinceCode,
        String districtCode,
        String communeCode,
        String villageCode,
        String addressLine1,
        String addressLine2,
        String postalCode,
        Double latitude,
        Double longitude,
        Boolean hasFan,
        Boolean hasAirConditioner,
        Boolean hasParking,
        Boolean hasPrivateBathroom,
        Boolean hasBalcony,
        Boolean hasKitchen,
        Boolean hasFridge,
        Boolean hasWashingMachine,
        Boolean hasTV,
        Boolean hasWiFi,
        Boolean hasElevator,
        Integer maxOccupants,
        Boolean isPetFriendly,
        Boolean isSmokingAllowed,
        Boolean isSharedRoom,
        GenderPreference genderPreference,
        Boolean isUtilityIncluded,
        Boolean depositRequired,
        Double depositAmount,
        Integer minStayMonths,
        String contactPhone,
        RoomStatus status
) {

}
