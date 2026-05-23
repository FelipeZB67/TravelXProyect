import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TravelSearchRequestModel, TravelSearchResponseModel } from '../models/travel-search.model';

@Injectable({ providedIn: 'root' })
export class TravelSearchService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/travel-search`;

  buscarTodo(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/searchjson`, request);
  }

  buscarTransporte(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/transportjson`, request);
  }

  buscarHospedaje(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/lodgingjson`, request);
  }
}
