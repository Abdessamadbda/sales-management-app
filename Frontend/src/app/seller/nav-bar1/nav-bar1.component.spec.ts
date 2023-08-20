import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavBar1Component } from './nav-bar1.component';

describe('NavBar1Component', () => {
  let component: NavBar1Component;
  let fixture: ComponentFixture<NavBar1Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NavBar1Component]
    });
    fixture = TestBed.createComponent(NavBar1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
