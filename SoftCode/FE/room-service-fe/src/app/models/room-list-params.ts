import { PropertyType, RoomType } from "./enum";

export interface RoomListParams {
    page?: number;
    size?: number;
    sortBy?: 'createdAt' | 'price' | 'viewCount' | 'name';
    direction?: 'asc' | 'desc';
    roomType?: RoomType | null;
    propertyType?: PropertyType | null;
    price?: number;
    priceMin: number | null;
    priceMax: number | null;
    provinceCode?: string | null;
    districtCode?: string | null;
    communeCode?: string | null;
    villageCode?: string | null;
    hasWiFi?: boolean | null;
    hasAirConditioner?: boolean | null;
    hasParking?: boolean | null;
    hasPrivateBathroom?: boolean | null;
    hasKitchen?: boolean | null;
    hasWashingMachine?: boolean | null;
      nearBy?: boolean | null;
  lat?: number | null;
  lon?: number | null;
  radiusMeters?: number | null;
}
