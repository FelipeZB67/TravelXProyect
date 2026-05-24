import { TestBed } from '@angular/core/testing';

import { TravelSearchService } from './travel-search.service';

describe('TravelSearchService', () => {
  let service: TravelSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TravelSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
