import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { Room } from '../../models/room';
import { RoomService } from '../../services/room.service';
import { RoomListParams } from '../../models/room-list-params';
import { PropertyCardComponent } from "../property-card/property-card.component";
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { RoomVM } from '../../models/room-vm';

@Component({
  selector: 'app-properties-grid',
  imports: [PropertyCardComponent],
  templateUrl: './properties-grid.component.html',
  styleUrl: './properties-grid.component.css'
})
export class PropertiesGridComponent{
  
  rooms = input.required<RoomVM[]>(); 
  favoriteToggle = output<string>(); // roomId

}
