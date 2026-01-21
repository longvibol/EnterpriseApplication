package com.piseth.java.school.roomservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("visitor_rooms")
@CompoundIndexes({
    @CompoundIndex(name = "idx_status_availableFrom", def = "{'status': 1, 'availableFrom': 1}"),
    @CompoundIndex(name = "idx_addr_province_district", def = "{'address.provinceCode': 1, 'address.districtCode': 1}")
})
public class Room {
	
  @Id
  private String id; // same as Room aggregate id

  private String ownerId;
  private String name;
  private String description;
  private Double price;
  private String currencyCode;
  private Integer floor;
  private Double roomSize;
  private String roomType;
  private String propertyType;

  private Address address;
  
  /**
   * GeoJSON Point for geo queries.
   * Mongo expects coordinates in order: [longitude, latitude].
   */

  @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
  private GeoJsonPoint geoPoint;

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
  private String genderPreference;

  private Double distanceToCenter;
  private List<String> nearbyLandmarks;
  private Boolean isUtilityIncluded;
  private Boolean depositRequired;
  private Double depositAmount;
  private Integer minStayMonths;
  private String contactPhone;

  private List<String> photoObjectKeys;
  //private List<String> photoUrls;
  private String videoUrl;
  private Boolean verifiedListing;

  private String status;
  private LocalDateTime availableFrom;
  private LocalDateTime availableTo;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;

  private Map<String, Object> extraAttributes;

  @Indexed
  private LocalDateTime lastEventAt;
  private boolean deleted; // soft delete 

}