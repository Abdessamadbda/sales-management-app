import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Aprofile1Component } from './aprofile1.component';

describe('Aprofile1Component', () => {
  let component: Aprofile1Component;
  let fixture: ComponentFixture<Aprofile1Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [Aprofile1Component]
    });
    fixture = TestBed.createComponent(Aprofile1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
