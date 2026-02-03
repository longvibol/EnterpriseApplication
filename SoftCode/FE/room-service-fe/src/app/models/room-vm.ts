import { Room } from './room';

export interface AmenityVM {
  icon: string;
  label: string;
}

export interface RoomVM {
  room: Room;

  mainPhoto: string;
  photoCount: number;

  priceText: string;
  addressText: string;
  availableText: string;

  statusText: string;
  statusClass: string;

  amenities: AmenityVM[];
  roomTypeText: string;
  propertyTypeText: string;
  isFavorite: boolean;
  distanceMeters?: number | null;
  distanceText?: string | null;
}
