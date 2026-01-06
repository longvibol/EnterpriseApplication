import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RoomListParams } from '../../models/room-list-params';
import { AddressService, AdminAreaResponse } from '../../services/address.service';
import { debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-properties-sidebar',
  imports: [ ReactiveFormsModule],
  templateUrl: './properties-sidebar.component.html',
  styleUrl: './properties-sidebar.component.css'
})
export class PropertiesSidebarComponent {

  filterChange = output<Partial<RoomListParams>>();

  private fb = inject(FormBuilder);
  private addressService = inject(AddressService);

  provinces: AdminAreaResponse[] = [];
  districts: AdminAreaResponse[] = [];
  communes: AdminAreaResponse[] = [];
  villages: AdminAreaResponse[] = [];

  constructor(){
    this.addressService.getProvinces().subscribe(list =>{
      this.provinces = list ?? [];
    });

    // Auto apply advanced filters
    this.form.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b))
      )
      .subscribe(() => {
        this.emitCurrent();
      });
  }

  private emitCurrent() {
    const raw: any = this.form.getRawValue();

    // Optional: include names for chips (UI only; backend can ignore)
    const provinceName = this.provinces.find(x => x.code === raw.provinceCode)?.nameEn ?? null;
    const districtName = this.districts.find(x => x.code === raw.districtCode)?.nameEn ?? null;
    const communeName = this.communes.find(x => x.code === raw.communeCode)?.nameEn ?? null;
    const villageName = this.villages.find(x => x.code === raw.villageCode)?.nameEn ?? null;

    this.filterChange.emit({
      page: 0, // reset page when any filter changes
      provinceCode: raw.provinceCode || null,
      districtCode: this.districtCtrl.enabled ? raw.districtCode || null : null,
      communeCode: this.communeCtrl.enabled ? raw.communeCode || null : null,
      villageCode: this.villageCtrl.enabled ? raw.villageCode || null : null,

      hasWiFi: raw.hasWiFi ? true : null,
    hasAirConditioner: raw.hasAirConditioner ? true : null,
    hasParking: raw.hasParking ? true : null,
    hasPrivateBathroom: raw.hasPrivateBathroom ? true : null,
    hasKitchen: raw.hasKitchen ? true : null,
    hasWashingMachine: raw.hasWashingMachine ? true : null,

      // UI-friendly names for chips (optional)
      provinceName: provinceName,
      districtName: districtName,
      communeName: communeName,
      villageName: villageName
    } as any);
  }


  form = this.fb.group({
    provinceCode: this.fb.control<string>(''),
    districtCode: this.fb.control<string>({value: '', disabled: true}),
    communeCode: this.fb.control<string>({value: '', disabled: true}),
    villageCode: this.fb.control<string>({value: '', disabled: true}),
    // new fields
    hasWiFi: this.fb.control<boolean>(false),
    hasAirConditioner: this.fb.control<boolean>(false),
    hasParking: this.fb.control<boolean>(false),
    hasPrivateBathroom: this.fb.control<boolean>(false),
    hasKitchen: this.fb.control<boolean>(false),
    hasWashingMachine: this.fb.control<boolean>(false)
  })

  resetAll() {
    this.districts = [];
    this.communes = [];
    this.villages = [];

    this.form.reset(
      {
        provinceCode: '',
        districtCode: { value: '', disabled: true } as any,
        communeCode: { value: '', disabled: true } as any,
        villageCode: { value: '', disabled: true } as any,
        hasWiFi: false,
        hasAirConditioner: false,
        hasParking: false,
        hasPrivateBathroom: false,
        hasKitchen: false,
        hasWashingMachine: false,
      },
      { emitEvent: false }
    );

    this.emitCurrent();
  }

  get provinceCtrl() {
    return this.form.controls.provinceCode;
  }

  get districtCtrl() {
    return this.form.controls.districtCode;
  }

  get communeCtrl() {
    return this.form.controls.communeCode;
  }

  get villageCtrl() {
    return this.form.controls.villageCode;
  }

  onProvinceChange(code: string){
    // clear old data from other combobox
    this.districts = [];
    this.communes = [];
    this.villages = [];
    this.form.patchValue({districtCode: '', communeCode: '', villageCode: ''}, { emitEvent: false });

    // Enable district
    if(!code){
      this.districtCtrl.disable();
      this.communeCtrl.disable();
      this.villageCtrl.disable();
      return;
    }

    this.districtCtrl.enable();
    this.communeCtrl.disable();
    this.villageCtrl.disable();

    this.addressService.getDistricts(code).subscribe(list =>{
      this.districts = list;
    })
  }

  onDistrictChange(code: string){
    this.communes = [];
    this.villages = [];
    this.form.patchValue({communeCode: '', villageCode: ''}, { emitEvent: false });

    if(!code){
      this.communeCtrl.disable();
      this.villageCtrl.disable();
      return;
    }

    this.communeCtrl.enable();
    this.villageCtrl.disable();
    this.addressService.getCommunes(code)
      .subscribe(list =>{
        this.communes = list;
      });
  }

  onCommuneChange(code: string){
    this.villages = [];
    this.form.patchValue({villageCode: ''}, { emitEvent: false });
    if(!code){
      this.villageCtrl.disable();
      return;
    }
    this.villageCtrl.enable();
    this.addressService.getVillages(code)
      .subscribe(list =>{
        this.villages = list;
      });

  }

  
}