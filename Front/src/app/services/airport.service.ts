import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AirportCodeModel {
  query?: string;
  countryCode?: string;
  iataCode?: string;
  name?: string;
  found?: boolean;
  message?: string;
  providerResponse?: string;
}

@Injectable({ providedIn: 'root' })
export class AirportService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/google-flights-airport`;

  buscarAeropuerto(query: string, countryCode: string): Observable<AirportCodeModel> {
    const params = new HttpParams()
      .set('query', query)
      .set('countryCode', countryCode);

    return this.http.get<AirportCodeModel>(`${this.urlBase}/search`, { params });
  }
}
