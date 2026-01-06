import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyActiveFilterBarComponent } from './property-active-filter-bar.component';

describe('PropertyActiveFilterBarComponent', () => {
  let component: PropertyActiveFilterBarComponent;
  let fixture: ComponentFixture<PropertyActiveFilterBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropertyActiveFilterBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PropertyActiveFilterBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
