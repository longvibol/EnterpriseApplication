import { inject, Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { RoomListParams } from '../models/room-list-params';
import { Observable } from 'rxjs';
import { Page } from '../models/page';
import { Room } from '../models/room';
import { HttpClient, HttpParams } from '@angular/common/http';
import { buildParams } from '../utils/param-util';
import { buildNearbyParams } from '../utils/nearby-param-util';
//import { buildParams } from '../core/http/utils';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  // api_url
  // request param
  private http = inject(HttpClient);
  private base = `${environment.apiUrl}`;

  ///room/search/pagination

  constructor() { }

  list(params?: RoomListParams ) : Observable<Page<Room>>{
    return this.http.get<Page<Room>>(this.base + "/rooms/search/pagination", {params: buildParams(params)});
  }

 /** GET /rooms/{id} */
  getById(id: string): Observable<Room> {
    return this.http.get<Room>(`${this.base}/rooms/${id}`);
  }

  getByIds(ids: string[]): Observable<Room[]> {
    const params = buildParams({ ids }); //  buildParams supports arrays 
    return this.http.get<Room[]>(`${this.base}/rooms/by-ids`, { params });
  }

    nearby(params: RoomListParams): Observable<Page<Room>> {
      // expects: lat, lon, radiusMeters, page, size + other filters like roomType, propertyType, priceMin/priceMax, etc.
      return this.http.get<Page<Room>>(this.base + "/rooms/nearby", { params: buildNearbyParams(params) });
    }

}
