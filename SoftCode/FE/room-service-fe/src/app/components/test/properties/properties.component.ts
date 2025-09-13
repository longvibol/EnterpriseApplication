import { Component, inject, OnInit, signal } from '@angular/core';
import { Room } from '../../models/room';
import { RoomService } from '../../services/room.service';
import { RoomListParams } from '../../models/room-list-params';
import { PropertiesListComponent } from '../properties-list/properties-list.component';
import { PropertyCardComponent } from '../property-card/property-card.component';

@Component({
  selector: 'app-properties',
  imports: [PropertiesListComponent, PropertyCardComponent],
  templateUrl: './properties.component.html',
  styleUrl: './properties.component.css'
})
export class PropertiesComponent{
// we inject roomService to use 
private roomService = inject(RoomService);
rooms = signal<Room[]>([]);

// rooms: Room[] =[];  // this is old style Zone 

//our custom params 
params: RoomListParams ={
  page: 0, size: 5,
  priceMin: null,
  priceMax: null
};

// Life cycle when start up reload the data 

ngOnInit(){
  this.loadData();
}

//crete function to use room service 
loadData(){
  this.roomService.list(this.params).subscribe(data =>{
    console.log(data);
    this.rooms.set(data.content);
    //this.rooms = data.content; this is Zone old style 

  });
}

}
