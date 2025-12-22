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
});

constructor() {
  // Sync incoming filter -> form (when parent changes)
  effect(() => {
    // we use effect to make the filter auto
    const f = this.filter();
    this.form.patchValue(
      //path is we waiting only the value change
      {
        propertyType: (f.propertyType ) ?? '',
        roomType: (f.roomType) ?? null,
        priceMin: f.priceMin ?? null,
        priceMax: f.priceMax ?? null
      },
      { emitEvent: false }
    );
  });

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
       priceMax: v.priceMax ?? null
     });
   });
}

setRoomType(type: RoomType | null) {
 this.form.patchValue({ roomType: type });
}
clearAll() {
  this.form.reset(
    { propertyType: null, roomType: null, priceMin: null, priceMax: null },
    { emitEvent: true }
  );
  this.filterChange.emit({
    page: 0,
    propertyType: null,
    roomType: null,
    priceMin: null,
    priceMax: null
  });
}

// Hook to open your existing location selector (sidebar or modal later)
openLocationPicker() {
  // For now: you can scroll to sidebar or open a modal in next step.
  // Keep simple:
  //document.getElementById('advancedFilters')?.scrollIntoView({ behavior: 'smooth' });
}
}