import { Component, signal } from '@angular/core';
import { PropertiesHeaderComponent } from "../properties-header/properties-header.component";
import { PropertiesGridComponent } from "../properties-grid/properties-grid.component";
import { PropertiesListComponent } from "../properties-list/properties-list.component";
import { PaginationComponent } from "../pagination/pagination.component";
import { PropertiesSidebarComponent } from "../properties-sidebar/properties-sidebar.component";
import { RoomListParams } from '../../models/room-list-params';
import { PropertyFilterBarComponent } from "../property-filter-bar/property-filter-bar.component";
import { ViewMode, SortOption } from '../../models/sort-option';
import { PropertyActiveFilterBarComponent } from "../property-active-filter-bar/property-active-filter-bar.component";

@Component({
  selector: 'app-section',
  imports: [PropertiesHeaderComponent, PropertiesGridComponent, PropertiesListComponent, PaginationComponent, PropertiesSidebarComponent, PropertyFilterBarComponent, PropertyActiveFilterBarComponent],
  templateUrl: './section.component.html',
  styleUrl: './section.component.css'
})
export class SectionComponent {

totalPages = signal<number>(0);
  total = signal<number | null>(null);

  onPageInfo(info: { totalPages: number; totalElements: number }) {
    this.totalPages.set(info.totalPages);
    this.total.set(info.totalElements);
  }


  filter = signal<RoomListParams>({
    page: 0,
    size: 4,
    priceMin: null,
    priceMax: null,
    provinceCode: null,
    districtCode: null,
    communeCode: null,
    villageCode: null,
    roomType: null,
    propertyType: null
  } );

  onFilterPatch(patch: Partial<RoomListParams>) {
    const current = this.filter();
    this.filter.set({ ...current, ...patch });
  }

  onClearAll() {
    const current = this.filter();
    this.filter.set({
      ...current,
      page: 0,
      priceMin: null,
      priceMax: null,
      provinceCode: null,
      districtCode: null,
      communeCode: null,
      villageCode: null,
      roomType: null,
      propertyType: null,
  
      hasWiFi: false,
      hasAirConditioner: false,
      hasParking: false,
      hasPrivateBathroom: false,
      hasKitchen: false,
      hasWashingMachine: false
    } );
  }

  viewMode = signal<ViewMode>('grid');
  sort = signal<SortOption>('NEWEST');

  onViewModeChange(mode: ViewMode) {
    this.viewMode.set(mode);
  }

  onSortChange(sort: SortOption) {
    this.sort.set(sort);
    this.onFilterPatch({ page: 0, sort }); 
  }

}