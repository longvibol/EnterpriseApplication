import { Component, computed, input } from '@angular/core';
import { Room } from '../../models/room';

type Amenity = { key: string; label: string; icon: string; enabled: boolean };
@Component({
  selector: 'app-property-amenities',
  imports: [],
  templateUrl: './property-amenities.component.html',
  styleUrl: './property-amenities.component.css'
})
export class PropertyAmenitiesComponent {
  room = input.required<Room>();

  amenities = computed<Amenity[]>(() => {
    const r = this.room();
    const items: Amenity[] = [
      { key: 'hasWiFi', label: 'Wi-Fi', icon: 'bi-wifi', enabled: !!r.hasWiFi },
      { key: 'hasAirConditioner', label: 'Air Conditioner', icon: 'bi-snow', enabled: !!r.hasAirConditioner },
      { key: 'hasFan', label: 'Fan', icon: 'bi-wind', enabled: !!r.hasFan },
      { key: 'hasPrivateBathroom', label: 'Private Bathroom', icon: 'bi-droplet', enabled: !!r.hasPrivateBathroom },
      { key: 'hasKitchen', label: 'Kitchen', icon: 'bi-egg-fried', enabled: !!r.hasKitchen },
      { key: 'hasFridge', label: 'Fridge', icon: 'bi-archive', enabled: !!r.hasFridge },
      { key: 'hasWashingMachine', label: 'Washing Machine', icon: 'bi-arrow-repeat', enabled: !!r.hasWashingMachine },
      { key: 'hasTV', label: 'TV', icon: 'bi-tv', enabled: !!r.hasTV },
      { key: 'hasBalcony', label: 'Balcony', icon: 'bi-building', enabled: !!r.hasBalcony },
      { key: 'hasParking', label: 'Parking', icon: 'bi-car-front', enabled: !!r.hasParking },
      { key: 'hasElevator', label: 'Elevator', icon: 'bi-arrow-up-square', enabled: !!r.hasElevator },
    ];

    return items.filter(x => x.enabled);
  });
}