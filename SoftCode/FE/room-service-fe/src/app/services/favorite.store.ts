import { Injectable, computed, signal } from '@angular/core';

const STORAGE_KEY = 'favorites_rooms_v1';

type StoredFavorites = {
  ids: string[];
  expiresAt: number; // epoch millis
};

const DEFAULT_TTL_MS = 1000 * 60 * 60 * 24 * 7; // 7 days (realistic)

function now(): number {
  return Date.now();
}

function readFromStorage(): StoredFavorites | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) {
      return null;
    }
    return JSON.parse(raw) as StoredFavorites;
  } catch {
    return null;
  }
}

function writeToStorage(data: StoredFavorites): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
}

function removeStorage(): void {
  localStorage.removeItem(STORAGE_KEY);
}

@Injectable({ providedIn: 'root' })
export class FavoritesStore {
  private readonly ttlMs = DEFAULT_TTL_MS;

  private readonly idsSig = signal<Set<string>>(new Set<string>());
  private readonly expiresAtSig = signal<number>(0);

  readonly ids = computed(() => this.idsSig());
  readonly count = computed(() => this.idsSig().size);
  readonly expiresAt = computed(() => this.expiresAtSig());
  readonly isExpired = computed(() => {
    const exp = this.expiresAtSig();
    return exp > 0 && now() >= exp;
  });

  constructor() {
    this.hydrate();
  }

  /** Load from localStorage; auto-clear if expired */
  private hydrate(): void {
    const stored = readFromStorage();
    if (!stored) {
      this.idsSig.set(new Set<string>());
      this.expiresAtSig.set(0);
      return;
    }

    if (stored.expiresAt && now() >= stored.expiresAt) {
      this.clear();
      return;
    }

    this.idsSig.set(new Set(stored.ids ?? []));
    this.expiresAtSig.set(stored.expiresAt ?? (now() + this.ttlMs));
  }

  /** Persist current ids and refresh TTL */
  private persistAndRefreshTtl(): void {
    const exp = now() + this.ttlMs;
    this.expiresAtSig.set(exp);
    writeToStorage({ ids: Array.from(this.idsSig()), expiresAt: exp });
  }

  isFavorite(roomId: string): boolean {
    if (this.isExpired()) {
      this.clear();
      return false;
    }
    return this.idsSig().has(roomId);
  }

  toggle(roomId: string): void {
    if (!roomId) {
      return;
    }
    if (this.isExpired()) {
      this.clear();
    }

    const next = new Set(this.idsSig());
    if (next.has(roomId)) {
      next.delete(roomId);
    } else {
      next.add(roomId);
    }
    this.idsSig.set(next);

    // If set becomes empty, remove storage (clean)
    if (next.size === 0) {
      this.expiresAtSig.set(0);
      removeStorage();
      return;
    }

    this.persistAndRefreshTtl();
  }

  clear(): void {
    this.idsSig.set(new Set<string>());
    this.expiresAtSig.set(0);
    removeStorage();
  }
}