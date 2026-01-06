import { Component, computed, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RoomVM } from '../../models/room-vm';
import { ViewMode } from '../../models/sort-option';
import { FavoritesStore } from '../../services/favorite.store';
import { LoadState } from '../../services/properties.facade';
import { RoomService } from '../../services/room.service';
import { toRoomVM } from '../../utils/room-formatter';
import { PropertiesGridComponent } from "../properties-grid/properties-grid.component";
import { PropertiesListComponent } from "../properties-list/properties-list.component";

@Component({
  selector: 'app-favorite-room',
  imports: [PropertiesGridComponent, PropertiesListComponent],
  templateUrl: './favorite-room.component.html',
  styleUrl: './favorite-room.component.css'
})
export class FavoriteRoomComponent {
  viewMode = signal<ViewMode>('grid');

  state = signal<LoadState>('idle');
  errorMessage = signal<string | null>(null);

  rooms = signal<RoomVM[]>([]);

  count = computed(() => this.favorites.count());
  expiresAt = computed(() => this.favorites.expiresAt());
  hasFavorites = computed(() => this.favorites.count() > 0);

  constructor(
    private readonly favorites: FavoritesStore,
    private readonly roomService: RoomService
  ) {
    this.load();
  }

  load(): void {
    this.errorMessage.set(null);

    const ids = Array.from(this.favorites.ids());
    if (ids.length === 0) {
      this.rooms.set([]);
      this.state.set('success');
      return;
    }

    this.state.set('loading');

    this.roomService.getByIds(ids)
      .pipe(takeUntilDestroyed())
      .subscribe({
        next: (list) => {
          // Keep same ordering as ids
          const map = new Map(list.map(r => [r.id, r]));
          const ordered = ids.map(id => map.get(id)).filter(Boolean) as any[];

          this.rooms.set(ordered.map(r => toRoomVM(r, true)));
          this.state.set('success');
        },
        error: (err) => {
          const msg =
            (err && (err.message ?? err.error?.message)) ?
              (err.message ?? err.error?.message) :
              'Failed to load saved rooms';
          this.errorMessage.set(msg);
          this.state.set('error');
        }
      });
  }
toggleFavorite(roomId: string): void {
    this.favorites.toggle(roomId);

    // remove from this list immediately if unfavorited
    this.rooms.update(list => list.filter(vm => this.favorites.isFavorite(vm.room.id)));

    // We can call load() instead (slower but simplest):
    // this.load();
  }

  clearAll(): void {
    this.favorites.clear();
    this.rooms.set([]);
  }

  switchView(mode: ViewMode): void {
    this.viewMode.set(mode);
  }
}