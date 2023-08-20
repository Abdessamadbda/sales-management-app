import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SstatesComponent } from './sstates.component';

describe('StatesComponent', () => {
  let component: SstatesComponent;
  let fixture: ComponentFixture<SstatesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SstatesComponent]
    });
    fixture = TestBed.createComponent(SstatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
