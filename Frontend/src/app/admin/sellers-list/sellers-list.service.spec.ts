import { TestBed } from '@angular/core/testing';

import { SellersListService } from './sellers-list.service';

describe('SellersListService', () => {
  let service: SellersListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SellersListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
