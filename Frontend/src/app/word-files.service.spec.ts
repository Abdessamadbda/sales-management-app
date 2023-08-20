import { TestBed } from '@angular/core/testing';

import { WordFilesService } from './admin/states/word-files.service';

describe('WordFilesService', () => {
  let service: WordFilesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WordFilesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
