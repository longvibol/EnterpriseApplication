package com.piseth.java.school.roomownerservice.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
	// Codes normalized to your AdminArea
	private String provinceCode;
	private String districtCode;
	private String communeCode;
	private String villageCode;

	// Optional human-readable names (denormalized snapshot for display)
	private String provinceName;
	private String districtName;
	private String communeName;
	private String villageName;

	// Free-form full address (house no., street, building)
	private String line1; // e.g., "House 12, St 2004, Borey ..."
	private String line2; // optional
	private String postalCode; // optional

	// Helpful for UI filters/search
	private List<String> nearbyLandmarks; // ["University", "Mall"]

	// Geo for maps & radius queries
	private GeoLocation geo; // {lat, lon}
}