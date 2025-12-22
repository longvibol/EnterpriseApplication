import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyFilterBarComponent } from './property-filter-bar.component';

describe('PropertyFilterBarComponent', () => {
  let component: PropertyFilterBarComponent;
  let fixture: ComponentFixture<PropertyFilterBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropertyFilterBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PropertyFilterBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
