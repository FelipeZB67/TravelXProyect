import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ResolvedLocationModel } from '../models/travel-search.model';

@Injectable({ providedIn: 'root' })
export class LocationService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/nominatim`;

  validarCiudadPais(city: string, country: string): Observable<ResolvedLocationModel> {
    const params = new HttpParams()
      .set('city', city)
      .set('country', country);

    return this.http.get<ResolvedLocationModel>(`${this.urlBase}/search`, { params });
  }
}
