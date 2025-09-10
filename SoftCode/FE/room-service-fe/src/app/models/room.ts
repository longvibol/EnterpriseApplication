import { GenderPreference, PropertyType, RoomType } from "./enum";
import { RoomLocation } from "./room-location";

export interface Room {
    id ?: string;
    name ?: string;
    price ?: number;
    floor ?: number;
    roomSize ?: number;
    location ?: RoomLocation;
    hasFan ?:boolean;
    hasAirConditioner ?:boolean;
    hasParking ?:boolean;
     hasPrivateBathroom?: boolean;
    hasBalcony?: boolean;
    hasKitchen?: boolean;
    hasFridge?: boolean;
    hasWashingMachine?: boolean;
    hasTV?: boolean;
    hasWiFi?: boolean;
    hasElevator?: boolean;
    maxOccupants?: number;
    isPetFriendly?: boolean;
    isSmokingAllowed?: boolean;
    isSharedRoom?: boolean;
    genderPreference?: GenderPreference;

    roomType?: RoomType;
    propertyType?: PropertyType;

    distanceToCenter?: number;
    nearbyLandmarks?: string[];

    isUtilityIncluded?: boolean;
    depositRequired?: boolean;
    minStayMonths?: number;

    hasPhotos?: boolean;
    photoCount?: number;
    hasVideoTour?: boolean;

    verifiedListing?: boolean;

    availableFrom?: string;   // ISO string
    availableTo?: string;     // ISO string

    createdAt?: string;
    lastUpdated?: string;

    extraAttributes?: Record<string, any>; 


}

/*

"content": [
        {
            "id": "68a28bcf3db7c1f20d03afea",
            "name": "Cozy Studio Apartment",
            "price": 350.0,
            "floor": 3,
            "roomSize": 28.5,
            "location": {
                "country": null,
                "city": "Phnom Penh",
                "district": "Chamkarmon",
                "street": null,
                "fullAddress": null
            },
            "hasFan": true,
            "hasAirConditioner": true,
            "hasParking": false,
            "hasPrivateBathroom": true,
            "hasBalcony": true,
            "hasKitchen": true,
            "hasFridge": true,
            "hasWashingMachine": false,
            "hasTV": false,
            "hasWiFi": true,
            "hasElevator": true,
            "maxOccupants": 2,
            "isPetFriendly": false,
            "isSmokingAllowed": false,
            "isSharedRoom": false,
            "genderPreference": "NO_PREFERENCE",
            "roomType": "STUDIO",
            "propertyType": "APARTMENT",
            "distanceToCenter": 2.3,
            "nearbyLandmarks": [
                "university",
                "mall",
                "hospital"
            ],
            "isUtilityIncluded": true,
            "depositRequired": true,
            "minStayMonths": 3,
            "hasPhotos": true,
            "photoCount": 5,
            "hasVideoTour": false,
            "verifiedListing": true,
            "availableFrom": "2025-09-01T00:00:00",
            "availableTo": "2026-09-01T00:00:00",
            "createdAt": "2025-08-18T09:00:00",
            "lastUpdated": "2025-08-18T09:05:00",
            "extraAttributes": {
                "furnished": true,
                "internetSpeedMbps": 50,
                "security": "24/7"
            }
        }

        */
