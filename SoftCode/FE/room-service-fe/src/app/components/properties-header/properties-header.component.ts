import { Component, input, output } from '@angular/core';
import { ViewMode, SortOption } from '../../models/sort-option';

@Component({
  selector: 'app-properties-header',
  imports: [],
  templateUrl: './properties-header.component.html',
  styleUrl: './properties-header.component.css'
})
export class PropertiesHeaderComponent {
// state comes from parent (signals)
  viewMode = input.required<ViewMode>();
  sort = input.required<SortOption>();

  // optional: show result count
  total = input<number | null>(null);

  viewModeChange = output<ViewMode>();
  sortChange = output<SortOption>();

  setView(mode: ViewMode) {
    this.viewModeChange.emit(mode);
  }

  onSortChange(value: string) {
    this.sortChange.emit(value as SortOption);
  }
}
