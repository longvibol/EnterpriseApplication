
import { Injectable, computed, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { catchError, distinctUntilChanged, finalize, of, switchMap, tap } from 'rxjs';

import { RoomListParams } from '../models/room-list-params';
import { RoomService } from './room.service';
import { RoomVM } from '../models/room-vm';
import { toRoomVM } from '../utils/room-formatter';
import { FavoritesStore } from './favorite.store';

export interface PageInfo {
  totalPages: number;
  totalElements: number;
}

export type LoadState = 'idle' | 'loading' | 'success' | 'error';

/** Keep ONE canonical default. Add fields as your RoomListParams grows. */
export const DEFAULT_FILTER: RoomListParams = {
  page: 0,
  size: 4,
  sortBy: 'createdAt',
  direction: 'desc',

  priceMin: null,
  priceMax: null,

  provinceCode: null,
  districtCode: null,
  communeCode: null,
  villageCode: null,

  roomType: null,
  propertyType: null,

  // Booleans (include only if your interface has them)
  hasWiFi: false,
  hasAirConditioner: false,
  hasParking: false,
  hasPrivateBathroom: false,
  hasKitchen: false,
  hasWashingMachine: false,
    nearBy: null,
  lat: null,
  lon: null,
  radiusMeters: null
};

function shallowEqual(a: any, b: any): boolean {
  if (a === b) {
    return true;
  }
  const ak = Object.keys(a ?? {});
  const bk = Object.keys(b ?? {});
  if (ak.length !== bk.length) {
    return false;
  }
  for (const k of ak) {
    if (a[k] !== b[k]) {
      return false;
    }
  }
  return true;
}

@Injectable({ providedIn: 'root' })
export class PropertiesFacade {
  private readonly filterSig = signal<RoomListParams>({ ...DEFAULT_FILTER });

  readonly filter = computed(() => this.filterSig());

  readonly rooms = signal<RoomVM[]>([]);
  readonly pageInfo = signal<PageInfo>({ totalPages: 0, totalElements: 0 });

  readonly state = signal<LoadState>('idle');
  readonly errorMessage = signal<string | null>(null);

  readonly totalPages = computed(() => this.pageInfo().totalPages);
  readonly total = computed(() => this.pageInfo().totalElements);

  constructor(
    private readonly roomService: RoomService,
  private readonly favorites: FavoritesStore) {
    toObservable(this.filterSig)
      .pipe(
        distinctUntilChanged((a, b) => shallowEqual(a, b)),
        tap(() => {
          this.state.set('loading');
          this.errorMessage.set(null);
        }),
        switchMap(f => {
          const useNearby = f.nearBy === true && !!f.lat && !!f.lon;

          const call$ = useNearby
            ? this.roomService.nearby(f)
            : this.roomService.list(f);

          return call$.pipe(
            catchError(err => {
              const msg =
                (err && (err.message ?? err.error?.message)) ?
                  (err.message ?? err.error?.message) :
                  'Failed to load properties';
              this.errorMessage.set(msg);
              this.state.set('error');
              return of(null);
            }),
            finalize(() => {
              if (this.state() === 'loading') {
                this.state.set('success');
              }
            })
          );
        }),
        // switchMap(f =>
        //   this.roomService.list(f)
        //   .pipe(
        //     catchError(err => {
        //       const msg =
        //         (err && (err.message ?? err.error?.message)) ?
        //           (err.message ?? err.error?.message) :
        //           'Failed to load properties';
        //       this.errorMessage.set(msg);
        //       this.state.set('error');
        //       return of(null);
        //     }),
        //     finalize(() => {
        //       if (this.state() === 'loading') {
        //         this.state.set('success');
        //       }
        //     })
        //   )
          
        // ),
        takeUntilDestroyed()
      )
      .subscribe(page => {
        if (!page) {
          this.rooms.set([]);
          this.pageInfo.set({ totalPages: 0, totalElements: 0 });
          return;
        }

        this.rooms.set((page.content ?? []).map(r => toRoomVM(r, this.favorites.isFavorite(r.id!)))); 
        this.pageInfo.set({
          totalPages: page.totalPage ?? 0,
          totalElements: page.totalElements ?? 0
        });
      });
  }

  /** Merge patch; reset page unless caller explicitly sets page */
  patchFilter(patch: Partial<RoomListParams>): void {
    this.filterSig.update(curr => ({
      ...curr,
      ...patch,
      page: patch.page ?? 0
    }));
  }

  clearAll(): void {
    this.filterSig.set({ ...DEFAULT_FILTER });
  }

  toggleFavorite(roomId: string): void {
    this.favorites.toggle(roomId);

    // keep current list in sync immediately
    this.rooms.update(list =>
      list.map(vm => vm.room.id === roomId ? { ...vm, isFavorite: !vm.isFavorite } : vm)
    );
  }

}