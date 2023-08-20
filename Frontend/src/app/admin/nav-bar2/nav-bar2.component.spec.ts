import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBar2Component } from './nav-bar2.component';

describe('NavBar2Component', () => {
  let component: NavBar2Component;
  let fixture: ComponentFixture<NavBar2Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavBar2Component]
    });
    fixture = TestBed.createComponent(NavBar2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
