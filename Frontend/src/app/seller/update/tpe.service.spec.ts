import { TestBed } from '@angular/core/testing';

import { TpeService } from './tpe.service';

describe('TpeService', () => {
  let service: TpeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TpeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
