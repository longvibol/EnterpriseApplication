import { Routes } from '@angular/router';
import { SectionComponent } from './components/section/section.component';
import { PropertyDetailsComponent } from './components/property-details/property-details.component';
import { FavoriteRoomComponent } from './components/favorite-room/favorite-room.component';

export const routes: Routes = [
    {
        path: '', redirectTo: 'properties', pathMatch: 'full'
    },
    {
        path: 'properties', component: SectionComponent
    },
    {
        path: 'properties/:id', component: PropertyDetailsComponent
    },
    { path: 'favorites', component: FavoriteRoomComponent },
    {
        path: '**', redirectTo: 'properties'
    },

];
