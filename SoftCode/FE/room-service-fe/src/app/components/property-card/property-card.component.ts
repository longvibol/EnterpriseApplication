import { Component, computed, input, Input, output } from '@angular/core';
import { Room } from '../../models/room';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RoomVM } from '../../models/room-vm';
import { toRoomVM } from '../../utils/room-formatter';

@Component({
  selector: 'app-property-card',
  imports: [RouterLink, CommonModule],
  templateUrl: './property-card.component.html',
  styleUrl: './property-card.component.css'
})
export class PropertyCardComponent {

  //room = input.required<Room>();
  vm = input.required<RoomVM>();

  favoriteToggle = output<void>();
  // UI-only helpers
  hasMultiplePhotos = computed(() => this.vm().photoCount > 1);
}
