import { Component, DestroyRef, inject, signal } from '@angular/core';
import { PropertyGalleryComponent } from "../property-gallery/property-gallery.component";
import { PropertyDescriptionComponent } from "../property-description/property-description.component";
import { PropertyAmenitiesComponent } from "../property-amenities/property-amenities.component";
import { PropertyMapComponent } from "../property-map/property-map.component";
import { PropertyOverviewComponent } from "../property-overview/property-overview.component";
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { map, filter, tap, switchMap, catchError, of } from 'rxjs';
import { Room } from '../../models/room';
import { RoomService } from '../../services/room.service';

@Component({
  selector: 'app-property-details',
  imports: [PropertyGalleryComponent, 
    PropertyDescriptionComponent, 
    PropertyAmenitiesComponent, 
    PropertyMapComponent, 
    PropertyOverviewComponent],
  templateUrl: './property-details.component.html',
  styleUrl: './property-details.component.css'
})
export class PropertyDetailsComponent {
  private route = inject(ActivatedRoute);
  private roomService = inject(RoomService);
  private destroyRef = inject(DestroyRef);

  room = signal<Room | null>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  constructor() {
    this.route.paramMap
      .pipe(
        map(p => p.get('id')),
        filter((id): id is string => !!id),
        tap(() => { // side effect
          this.loading.set(true);
          this.error.set(null);
        }),
        switchMap(id =>
          this.roomService.getById(id).pipe(
            catchError(err => {
              this.error.set(err?.message ?? 'Failed to load room');
              return of(null);
            })
          )
        ),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(r => {
        this.room.set(r);
        this.loading.set(false);
      });
  }
}