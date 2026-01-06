import { Component, computed, inject, input } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Room } from '../../models/room';

@Component({
  selector: 'app-property-map',
  imports: [],
  templateUrl: './property-map.component.html',
  styleUrl: './property-map.component.css'
})
export class PropertyMapComponent {
  room = input.required<Room>();

  private sanitizer = inject(DomSanitizer);

  lat = computed(() => this.room().address?.geo?.latitude);
  lng = computed(() => this.room().address?.geo?.longitude);

  safeMapUrl = computed<SafeResourceUrl | null>(() => {
    const lat = this.lat();
    const lng = this.lng();

    if (lat == null || lng == null) {
      return null;
    }

    // keep numbers safe
    const latNum = Number(lat);
    const lngNum = Number(lng);

    if (!Number.isFinite(latNum) || !Number.isFinite(lngNum)) {
      return null;
    }

    const bbox = `${lngNum - 0.01},${latNum - 0.01},${lngNum + 0.01},${latNum + 0.01}`;
    const url = `https://www.openstreetmap.org/export/embed.html?bbox=${bbox}&layer=mapnik&marker=${latNum},${lngNum}`;

    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  });
}