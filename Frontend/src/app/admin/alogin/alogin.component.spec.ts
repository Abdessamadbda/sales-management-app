import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AloginComponent } from './alogin.component';

describe('AloginComponent', () => {
  let component: AloginComponent;
  let fixture: ComponentFixture<AloginComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AloginComponent]
    });
    fixture = TestBed.createComponent(AloginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
