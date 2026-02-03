import { HttpParams } from "@angular/common/http";
import { RoomListParams } from "../models/room-list-params";
import { buildParams } from "./param-util";

export function buildNearbyParams(filter: RoomListParams): HttpParams  {

    // 1. root params for NearbyRoomFilterDTO
    const root = buildParams({
        lat: filter.lat,
        lon: filter.lon,
        radiusMeters: filter.radiusMeters,
        page: filter.page,
        size: filter.size
    });

    // 2. other filters for RoomFilterDTO
    const other = buildParams({
        roomType: filter.roomType,
        propertyType: filter.propertyType,
        price: filter.price,
        priceMin: filter.priceMin,
        priceMax: filter.priceMax,
        provinceCode: filter.provinceCode,
        districtCode: filter.districtCode,
        communeCode: filter.communeCode,
        villageCode: filter.villageCode,
        hasWiFi: filter.hasWiFi,
        hasAirConditioner: filter.hasAirConditioner,
        hasParking: filter.hasParking,
        hasPrivateBathroom: filter.hasPrivateBathroom,
        hasKitchen: filter.hasKitchen,
        hasWashingMachine: filter.hasWashingMachine,
    });

    // rename keys => otherFilters.<key>
    let params = root;
    // HttpParams has no iterator, so we use keys() + getAll()
    for(const key of other.keys()){
        const values = other.getAll(key) ?? [];
        for(const v of values){
            params = params.append(`otherFilters.${key}`, v);
        }
    }
    return params;
}