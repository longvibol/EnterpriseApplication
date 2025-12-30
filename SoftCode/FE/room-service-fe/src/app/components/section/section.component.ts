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
import { PropertiesFacade } from '../../services/properties.facade';

@Component({
  selector: 'app-section',
  imports: [PropertiesHeaderComponent, PropertiesGridComponent, PropertiesListComponent, PropertiesSidebarComponent, PropertyFilterBarComponent, PropertyActiveFilterBarComponent, PaginationComponent],
  templateUrl: './section.component.html',
  styleUrl: './section.component.css'
})
export class SectionComponent {

  // UI-only state
  viewMode = signal<ViewMode>('grid');
  sort = signal<SortOption>('NEWEST');

  constructor(public readonly facade: PropertiesFacade) {}

  onViewModeChange(mode: ViewMode) {
    this.viewMode.set(mode);
  }

  onSortChange(sort: SortOption) {
    this.sort.set(sort);
    this.facade.patchFilter({ sort, page: 0 });
    // after we short we need to set the page to zero to make the data recreate 
  }

}