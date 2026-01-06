import { Component, computed, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
// 0-based page index from your filter
page = input.required<number>();

// total pages from API (e.g., Spring Page.totalPages)
totalPages = input.required<number>();

// how many page numbers to show (window)
maxButtons = input<number>(5);

pageChange = output<number>();

lastIndex = computed(() => Math.max(0, this.totalPages() - 1));

canPrev = computed(() => this.page() > 0);
canNext = computed(() => this.page() < this.lastIndex());

// Pages to show (0-based internally)
pages = computed(() => {
  const total = this.totalPages();
  if (total <= 0) {
    return [];
  }

  const current = this.page();
  const max = Math.max(3, this.maxButtons()); // minimum 3
  const half = Math.floor(max / 2);

  let start = Math.max(0, current - half);
  let end = Math.min(total - 1, start + max - 1);

  // shift start if we're at the end
  start = Math.max(0, end - max + 1);

  const out: number[] = [];
  for (let i = start; i <= end; i++) {
    out.push(i);
  }
  return out;
});

goTo(index: number) {
  const safe = Math.min(Math.max(index, 0), this.lastIndex());
  if (safe !== this.page()) {
    this.pageChange.emit(safe);
  }
}

prev() {
  if (this.canPrev()) {
    this.goTo(this.page() - 1);
  }
}

next() {
  if (this.canNext()) {
    this.goTo(this.page() + 1);
  }
}

first() {
  this.goTo(0);
}

last() {
  this.goTo(this.lastIndex());
}
}