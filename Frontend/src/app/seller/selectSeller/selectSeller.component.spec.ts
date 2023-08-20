import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectSellerComponent } from './selectSeller.component';

describe('SelectSellerComponent', () => {
  let component: SelectSellerComponent;
  let fixture: ComponentFixture<SelectSellerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectSellerComponent]
    });
    fixture = TestBed.createComponent(SelectSellerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
