import { Component, computed, input, output } from '@angular/core';
import { RoomListParams } from '../../models/room-list-params';

type Chip = {
  key: string;
  label: string;
  patch: Partial<RoomListParams>;
};
@Component({
  selector: 'app-property-active-filter-bar',
  imports: [],
  templateUrl: './property-active-filter-bar.component.html',
  styleUrl: './property-active-filter-bar.component.css'
})
export class PropertyActiveFilterBarComponent {
  filter = input.required<RoomListParams>();

  // When user clicks ✕ on a chip, we emit a patch to remove that filter
  remove = output<Partial<RoomListParams>>();

  clearAll = output<void>();

  chips = computed<Chip[]>(() => {
    const f: any = this.filter(); // allow optional UI fields if you add them

    const chips: Chip[] = [];

    // --- Location (prefer names if available, fallback to codes) ---
    if (f.provinceCode) {
      chips.push({
        key: 'provinceCode',
        label: `Province: ${f.provinceName ?? f.provinceCode}`,
        patch: { page: 0, provinceCode: null, districtCode: null, communeCode: null, villageCode: null }
      });
    }
    if (f.districtCode) {
      chips.push({
        key: 'districtCode',
        label: `District: ${f.districtName ?? f.districtCode}`,
        patch: { page: 0, districtCode: null, communeCode: null, villageCode: null }
      });
    }
    if (f.communeCode) {
      chips.push({
        key: 'communeCode',
        label: `Commune: ${f.communeName ?? f.communeCode}`,
        patch: { page: 0, communeCode: null, villageCode: null }
      });
    }
    if (f.villageCode) {
      chips.push({
        key: 'villageCode',
        label: `Village: ${f.villageName ?? f.villageCode}`,
        patch: { page: 0, villageCode: null }
      });
    }
// --- Price ---
if (f.priceMin != null || f.priceMax != null) {
  const min = f.priceMin != null ? f.priceMin : 'Any';
  const max = f.priceMax != null ? f.priceMax : 'Any';
  chips.push({
    key: 'price',
    label: `Price: ${min} – ${max}`,
    patch: { page: 0, priceMin: null, priceMax: null }
  });
}

// --- Room type ---
if (f.roomType) {
  chips.push({
    key: 'roomType',
    label: `Room: ${String(f.roomType).toLowerCase()}`,
    patch: { page: 0, roomType: null }
  });
}

if (f.nearBy === true && f.lat != null && f.lon != null) {
  const km = f.radiusMeters ? Math.round(f.radiusMeters / 1000) : 3;
  chips.push({
    key: 'nearBy',
    label: `Near me: ${km} km`,
    patch: { page: 0, nearBy: null, lat: null, lon: null, radiusMeters: null }
  });
}

// --- Property type ---
if (f.propertyType) {
  chips.push({
    key: 'propertyType',
    label: `Type: ${String(f.propertyType).toLowerCase()}`,
    patch: { page: 0, propertyType: null }
  });
}
// --- Amenities (only add if present in filter params) ---
const amenityFields: Array<[keyof RoomListParams, string]> = [
  ['hasWiFi' , 'WiFi'],
  ['hasAirConditioner' , 'AC'],
  ['hasParking', 'Parking'],
  ['hasPrivateBathroom', 'Private bath'],
  ['hasKitchen', 'Kitchen'],
  ['hasWashingMachine', 'Washer']
];

for (const [k, label] of amenityFields) {
  if (f[k] === true) {
    chips.push({
      key: String(k),
      label,
      patch: { page: 0, [k]: false }
    });
  }
}

return chips;
});

onRemove(chip: Chip) {
  this.remove.emit(chip.patch);
}

onClearAll() {
  this.clearAll.emit();
}
}
