import { Component, inject, input, output, signal } from '@angular/core';
import { toObservable, takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { Room } from '../../models/room';
import { RoomListParams } from '../../models/room-list-params';
import { RoomService } from '../../services/room.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RoomVM } from '../../models/room-vm';

@Component({
  selector: 'app-properties-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './properties-list.component.html',
  styleUrl: './properties-list.component.css'
})
export class PropertiesListComponent {
rooms = input.required<RoomVM[]>();
favoriteToggle = output<string>(); // roomId
}
