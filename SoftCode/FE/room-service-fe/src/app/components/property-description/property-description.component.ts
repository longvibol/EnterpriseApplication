import { Component, computed, input } from '@angular/core';
import { Room } from '../../models/room';

@Component({
  selector: 'app-property-description',
  imports: [],
  templateUrl: './property-description.component.html',
  styleUrl: './property-description.component.css'
})
export class PropertyDescriptionComponent {
  room = input.required<Room>();

  fullAddress = computed(() => {
    const a = this.room().address ?? {};
    const parts = [
      a.line1,
      a.line2,
      a.villageName,
      a.communeName,
      a.districtName,
      a.provinceName
    ].filter(Boolean);
    return parts.join(', ');
  });
}