import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TravelSearchService } from './travel-search.service';

describe('TravelSearchService', () => {
  let service: TravelSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(TravelSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
