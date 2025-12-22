import { Component, computed, input, Input } from '@angular/core';
import { Room } from '../../models/room';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-property-card',
  imports: [RouterLink, CommonModule],
  templateUrl: './property-card.component.html',
  styleUrl: './property-card.component.css'
})
export class PropertyCardComponent {

  room = input.required<Room>();

  mainPhoto = computed(() => {
    // recalculate when room change : computed funcotion
    const urls = this.room().photoUrls ?? [];
    return urls.length > 0 ? urls[0] : 'assets/img/real-estate/property-exterior-1.webp';
  });

  priceText = computed(() => {
    const r = this.room();
    const price = r.price ?? 0;
    const code = r.currencyCode ?? 'USD';

    if (!r.price || r.price <= 0) {
      return 'Contact';
    }

    try {
      return new Intl.NumberFormat(undefined, { style: 'currency', currency: code }).format(price);
    } catch {
      return `${price} ${code}`;
    }
  });

  addressText = computed(() => {
    const a: any = this.room().address ?? {};
    const parts = [
      a.line1,
      a.villageName,
      a.communeName,
      a.districtName,
      a.provinceName
    ].filter(Boolean);

    // filter boolean it take only the true value 
    return parts.join(', ');
  });

  roomTypeText = computed(() => {
    const v = this.room().roomType;
    if (!v) {
      return '';
    }
    return v.charAt(0) + v.slice(1).toLowerCase();
  });

  propertyTypeText = computed(() => {
    const v = this.room().propertyType;
    if (!v) {
      return '';
    }
    return v.charAt(0) + v.slice(1).toLowerCase();
  });

  availableText = computed(() => {
    const from = this.room().availableFrom;
    if (!from) {
      return '';
    }
    const d = new Date(from);
    if (Number.isNaN(d.getTime())) {
      return '';
    }
    return d.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: '2-digit' });
  });

  amenityIcons = computed(() => {
    const r = this.room();

    const items: { show: boolean; icon: string; label: string }[] = [
      { show: !!r.hasWiFi, icon: 'bi-wifi', label: 'WiFi' },
      { show: !!r.hasAirConditioner, icon: 'bi-snow', label: 'Air conditioner' },
      { show: !!r.hasParking, icon: 'bi-car-front', label: 'Parking' },
      { show: !!r.hasKitchen, icon: 'bi-egg-fried', label: 'Kitchen' },
      { show: !!r.hasPrivateBathroom, icon: 'bi-droplet', label: 'Private bathroom' },
      { show: !!r.hasBalcony, icon: 'bi-border', label: 'Balcony' },
      { show: !!r.hasElevator, icon: 'bi-building', label: 'Elevator' },
      { show: !!r.hasWashingMachine, icon: 'bi-bucket', label: 'Washing machine' }
    ];

    return items.filter(i => i.show).slice(0, 6);
  });

  statusBadgeClass = computed(() => {
    const s = this.room().status;
    if (s === 'AVAILABLE') {
      return 'badge-available';
    }
    if (s === 'RENTED') {
      return 'badge-rented';
    }
    return 'badge-hidden';
  });

  statusText = computed(() => {
    const s = this.room().status;
    if (s === 'AVAILABLE') {
      return 'Available';
    }
    if (s === 'RENTED') {
      return 'Rented';
    }
    return 'Hidden';
  });

}