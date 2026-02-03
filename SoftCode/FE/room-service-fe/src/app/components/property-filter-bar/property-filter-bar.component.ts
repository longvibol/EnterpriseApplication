import { Component, effect, inject, input, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { RoomType, PropertyType } from '../../models/enum';
import { RoomListParams } from '../../models/room-list-params';

@Component({
  selector: 'app-property-filter-bar',
  imports: [ReactiveFormsModule],
  templateUrl: './property-filter-bar.component.html',
  styleUrl: './property-filter-bar.component.css'
})
export class PropertyFilterBarComponent {
  readonly RoomType = RoomType;
  readonly PropertyType = PropertyType;

// current filter from parent (so bar can show existing selections)
filter = input.required<RoomListParams>();
//[filter]
// emits partial updates (parent merges)
filterChange = output<Partial<RoomListParams>>();
// (filterChange)
private fb = inject(FormBuilder);

form = this.fb.group({
  propertyType: this.fb.control<PropertyType | ''>(''),
  roomType: this.fb.control<RoomType | null>(null),
  priceMin: this.fb.control<number | null>(null, { validators: [Validators.min(0)] }),
  priceMax: this.fb.control<number | null>(null, { validators: [Validators.min(0)] }),

  nearBy: this.fb.control<boolean>(false),
    radiusMeters: this.fb.control<number>({ value: 3000, disabled: true }),
    lat: this.fb.control<number | null>(null),
    lon: this.fb.control<number | null>(null)
});

constructor() {
  // Sync incoming filter -> form (when parent changes)
  effect(() => {
    const f = this.filter();
    this.form.patchValue(
      {
        propertyType: (f.propertyType ) ?? '',
        roomType: (f.roomType) ?? null,
        priceMin: f.priceMin ?? null,
        priceMax: f.priceMax ?? null,
        nearBy: f.nearBy === true,
          radiusMeters: f.radiusMeters ?? 1000,
          lat: f.lat ?? null,
          lon: f.lon ?? null
      },
      { emitEvent: false }
    );
      if (f.nearBy === true) {
    this.form.controls.radiusMeters.enable({ emitEvent: false });
  } else {
    this.form.controls.radiusMeters.disable({ emitEvent: false });
  }
  });

  // this.form.controls.nearBy.valueChanges.subscribe(on => {
  //   if (on) {
  //     this.form.controls.radiusMeters.enable({ emitEvent: false });
  //   } else {
  //     this.form.controls.radiusMeters.disable({ emitEvent: false });
  //   }
  // });

   // Auto emit changes (quick filters feel “instant”)
   this.form.valueChanges
   .pipe(
     debounceTime(300),
     distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b))
   )
   .subscribe(v => {
     this.filterChange.emit({
       page: 0, // reset pagination on filter change
       propertyType: v.propertyType || null,
       roomType: v.roomType || null,
       priceMin: v.priceMin ?? null,
       priceMax: v.priceMax ?? null,
       nearBy: v.nearBy ? true : null,
          radiusMeters: v.nearBy ? (v.radiusMeters ?? 3000) : null,
          lat: v.nearBy ? (v.lat ?? null) : null,
          lon: v.nearBy ? (v.lon ?? null) : null
     });
   });

   this.form.controls.nearBy.valueChanges.subscribe(on => {
  if (on) {
    this.form.controls.radiusMeters.enable({ emitEvent: false });
  } else {
    this.form.controls.radiusMeters.disable({ emitEvent: false });
  }
});
}

setRoomType(type: RoomType | null) {
 this.form.patchValue({ roomType: type });
}
clearAll() {
  this.form.reset(
    { propertyType: null, 
      roomType: null, 
      priceMin: null, 
      priceMax: null,
    nearBy: false,
        radiusMeters: 3000,
        lat: null,
        lon: null
    },
    { emitEvent: true }
  );
  this.filterChange.emit({
    page: 0,
    propertyType: null,
    roomType: null,
    priceMin: null,
    priceMax: null,
    nearBy: false,
        radiusMeters: null,
        lat: null,
        lon: null
  });
}

async toggleNearBy() {
    const enabled = this.form.controls.nearBy.value === true;

    if (enabled) {
      // turn OFF
      this.form.patchValue({ nearBy: false, lat: null, lon: null }, { emitEvent: true });
      return;
    }

    // turn ON: request browser location
    if (!navigator.geolocation) {
      // fallback: keep off if not supported
      this.form.patchValue({ nearBy: false }, { emitEvent: true });
      return;
    }
navigator.geolocation.getCurrentPosition(
      pos => {
        this.form.patchValue(
          {
            nearBy: true,
            lat: pos.coords.latitude,
            lon: pos.coords.longitude,
            radiusMeters: this.form.controls.radiusMeters.value ?? 3000
          },
          { emitEvent: true }
        );
      },
      _err => {
        // user denied / error => keep it OFF
        this.form.patchValue({ nearBy: false, lat: null, lon: null }, { emitEvent: true });
      },
      { enableHighAccuracy: true, timeout: 8000, maximumAge: 60_000 }
    );
  }

// Hook to open your existing location selector (sidebar or modal later)
openLocationPicker() {
  // For now: you can scroll to sidebar or open a modal in next step.
  // Keep simple:
  //document.getElementById('advancedFilters')?.scrollIntoView({ behavior: 'smooth' });
}
}
