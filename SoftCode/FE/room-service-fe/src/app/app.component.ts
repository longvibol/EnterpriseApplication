import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { PageTitleComponent } from './components/page-title/page-title.component';
import { PropertiesComponent } from './components/properties/properties.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, PageTitleComponent,FooterComponent,HeaderComponent,PropertiesComponent],
  templateUrl: './app.component.html',
  styleUrls: [] // remove app.component.scss if file does not exist
})
export class AppComponent {}
