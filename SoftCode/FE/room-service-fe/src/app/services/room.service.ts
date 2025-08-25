import { inject, Injectable } from '@angular/core';
import { RoomListParams } from '../models/room-list-params';
import { Observable } from 'rxjs';
import { Page } from '../models/page';
import { Room } from '../models/room';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environment/environment';
import { buildParams } from '../core/http/utils';


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
}