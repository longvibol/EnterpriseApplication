import { Component, inject, output } from '@angular/core';
import { FeaturedPropertiesComponent } from "../featured-properties/featured-properties.component";
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { RoomListParams } from '../../models/room-list-params';
import { AddressService, AdminAreaResponse } from '../../services/address.service';

@Component({
  selector: 'app-properties-sidebar',
  imports: [FeaturedPropertiesComponent, ReactiveFormsModule, FormsModule],
  templateUrl: './properties-sidebar.component.html',
  styleUrl: './properties-sidebar.component.css'
})
export class PropertiesSidebarComponent {

  filterChange = output<RoomListParams>();
  private base: RoomListParams = {page: 0, size: 4, priceMin: null, priceMax: null}

  private fb = inject(FormBuilder);
  // called the addressServeic 

  private addressService = inject(AddressService);

  provinces: AdminAreaResponse[] =[];
  districts: AdminAreaResponse[] =[];
  communes: AdminAreaResponse[] =[];
  villages: AdminAreaResponse[] =[];

  constructor(){
    this.addressService.getProvinces().subscribe(list =>{
      this.provinces = list ?? [];
    })
  }

  // binding to the form submit
  form = this.fb.group({
    provinceCode: this.fb.control<string>(''),
    districtCode: this.fb.control<string>({value:'', disabled: true}),
    communeCode: this.fb.control<string>({value:'', disabled: true}),
    villageCode: this.fb.control<string>({value:'', disabled: true}),
    priceMin: this.fb.control<number | null> (null, {validators: [Validators.min(0)]}),
    priceMax: this.fb.control<number | null> (null, {validators: [Validators.min(0)]}),
  })

  // crate getter from From
  get provinceCtrl(){
    return this.form.controls.provinceCode;
  }
  get districtCtrl(){
    return this.form.controls.districtCode;
  }
  get communeCtrl(){
    return this.form.controls.communeCode;
  }
  get villageCtrl(){
    return this.form.controls.villageCode;
  }

  onProvinceChange(code : string){
    // clear old data from other combox 

    this.districts = [];
    this.communes = [];
    this.villages = [];
    this.form.patchValue({
      districtCode:'', communeCode:'', villageCode:''
    })

    //Enable district
    if(!code){
      this.districtCtrl.disable();
      this.communeCtrl.disable();
      this.villageCtrl.disable();

      // we return to finished this function
      return;
    }

      this.districtCtrl.enable();
      this.communeCtrl.disable();
      this.villageCtrl.disable();

    //we called our api 
    this.addressService.getDistricts(code).subscribe(list => {
      this.districts = list;
    })
  }

  applyFilter(){
    //console.log("Apply is clicked")
    //console.log(this.form.getRawValue())
    const {priceMin, priceMax}  = this.form.getRawValue();
    this.filterChange.emit({...this.base, priceMin: priceMin ?? null, priceMax: priceMax ?? null})
  }
}