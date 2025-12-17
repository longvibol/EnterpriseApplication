import { Component, computed, input, signal } from '@angular/core';
import { Room } from '../../models/room';

@Component({
  selector: 'app-property-gallery',
  imports: [],
  templateUrl: './property-gallery.component.html',
  styleUrl: './property-gallery.component.css'
})
export class PropertyGalleryComponent {
  room = input.required<Room>();

  activeIndex = signal<number>(0);

  images = computed(() => {
    const urls = this.room().photoUrls ?? [];
    if (urls.length > 0) {
      return urls;
    }
    return ['assets/img/real-estate/property-exterior-3.webp']; // fallback
  });

  setActive(i: number) {
    const max = this.images().length - 1;
    this.activeIndex.set(Math.min(Math.max(i, 0), max));
  }

  prev() {
    this.setActive(this.activeIndex() - 1);
  }

  next() {
    this.setActive(this.activeIndex() + 1);
  }

}