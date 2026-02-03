import { Component, computed, input } from '@angular/core';
import { Room } from '../../models/room';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-property-overview',
  imports: [DatePipe],
  templateUrl: './property-overview.component.html',
  styleUrl: './property-overview.component.css'
})
export class PropertyOverviewComponent {
  room = input.required<Room>();
  
  priceText = computed(() => {
    const r = this.room();
    const price = r.price ?? 0;
    const code = r.currencyCode ?? 'USD';
    try {
      return new Intl.NumberFormat(undefined, { style: 'currency', currency: code }).format(price);
    } catch {
      return `${price} ${code}`;
    }
  });

  addressText = computed(() => {
    const a = this.room().address ?? {};
    const parts = [a.line1, a.villageName, a.communeName, a.districtName, a.provinceName]
    .filter(Boolean);
    return parts.join(', ');
  });

  whatsappLink = computed(() => {
    const phone = (this.room().contactPhone ?? '').replace(/[^\d+]/g, '');
    if (!phone) {
      return null;
    }
    return `https://wa.me/${phone.replace('+', '')}`;
  });
  
}
