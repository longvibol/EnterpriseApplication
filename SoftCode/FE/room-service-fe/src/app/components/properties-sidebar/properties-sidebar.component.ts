import { Component, inject, output } from '@angular/core';
import { FeaturedPropertiesComponent } from "../featured-properties/featured-properties.component";
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RoomListParams } from '../../models/room-list-params';

@Component({
  selector: 'app-properties-sidebar',
  imports: [FeaturedPropertiesComponent, ReactiveFormsModule],
  templateUrl: './properties-sidebar.component.html',
  styleUrl: './properties-sidebar.component.css'
})
export class PropertiesSidebarComponent {

  filterChange = output<RoomListParams>();
  private base: RoomListParams = {page: 0, size: 4, priceMin: null, priceMax: null}

  private fb = inject(FormBuilder);

  form = this.fb.group({
    priceMin: this.fb.control<number | null> (null, {validators: [Validators.min(0)]}),
    priceMax: this.fb.control<number | null> (null, {validators: [Validators.min(0)]}),
  })

  applyFilter(){
    //console.log("Apply is clicked")
    //console.log(this.form.getRawValue())
    const {priceMin, priceMax}  = this.form.getRawValue();
    // this.filterChange.emit({...this.base, priceMin: priceMin ?? null, priceMax: priceMax ?? null})
  }
}