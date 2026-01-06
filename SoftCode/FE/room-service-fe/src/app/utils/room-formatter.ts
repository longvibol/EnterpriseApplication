import { Room } from '../models/room';
import { RoomVM } from '../models/room-vm';

export function formatPrice(price?: number | null, code?: string | null): string {
  const p = price ?? 0;
  const c = code ?? 'USD';

  if (!p || p <= 0) {
    return 'Contact';
  }

  try {
    return new Intl.NumberFormat(undefined, { style: 'currency', currency: c }).format(p);
  } catch {
    return `${p} ${c}`;
  }
}

export function formatAddress(room: Room): string {
  const a: any = room.address ?? {};
  return [a.line1, a.villageName, a.communeName, a.districtName, a.provinceName]
    .filter(Boolean)
    .join(', ');
}

export function formatAvailable(from?: string | null): string {
  if (!from) {
    return '';
  }
  const d = new Date(from);
  if (Number.isNaN(d.getTime())) {
    return '';
  }
  return d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: '2-digit' });
}

export function statusText(status?: string | null): string {
  if (status === 'AVAILABLE') {
    return 'Available';
  }
  if (status === 'RENTED') {
    return 'Rented';
  }
  return 'Hidden';
}

export function statusClass(status?: string | null): string {
  if (status === 'AVAILABLE') {
    return 'badge-available';
  }
  if (status === 'RENTED') {
    return 'badge-rented';
  }
  return 'badge-hidden';
}

export function amenityIcons(room: any): Array<{ icon: string; label: string }> {
  const items = [
    { ok: !!room.hasWiFi, icon: 'bi-wifi', label: 'WiFi' },
    { ok: !!room.hasAirConditioner, icon: 'bi-snow', label: 'AC' },
    { ok: !!room.hasParking, icon: 'bi-car-front', label: 'Parking' },
    { ok: !!room.hasKitchen, icon: 'bi-egg-fried', label: 'Kitchen' },
    { ok: !!room.hasPrivateBathroom, icon: 'bi-droplet', label: 'Private bath' },
    { ok: !!room.hasWashingMachine, icon: 'bi-bucket', label: 'Washer' },
    { ok: !!room.hasBalcony, icon: 'bi-border', label: 'Balcony' },
    { ok: !!room.hasElevator, icon: 'bi-building', label: 'Elevator' }
  ];

  return items.filter(x => x.ok).map(x => ({ icon: x.icon, label: x.label })).slice(0, 6);
}

export function toRoomVM(room: Room, isFavorite: boolean): RoomVM {
  const urls = room.photoUrls ?? [];
  const photoCount = urls.length;

  return {
    room,
    mainPhoto: photoCount > 0 ? urls[0] : 'assets/img/real-estate/property-exterior-1.webp',
    photoCount,
    priceText: formatPrice(room.price, room.currencyCode),
    addressText: formatAddress(room),
    availableText: formatAvailable(room.availableFrom),
    statusText: statusText(room.status as any),
    statusClass: statusClass(room.status as any),
    amenities: amenityIcons(room as any),
    roomTypeText: capitalize(room.roomType),
    propertyTypeText: capitalize(room.propertyType),
    isFavorite
  };
}

function capitalize(v?: string | null): string {
  if (!v) {
    return '';
  }
  return v.charAt(0) + v.slice(1).toLowerCase();
}