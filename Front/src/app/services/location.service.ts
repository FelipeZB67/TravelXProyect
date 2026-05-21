import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NominatimLocationModel } from '../models/nominatim-location.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class LocationService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/api/locations`;

  getCodigos(): Observable<string[]> {
    return this.http.get<string[]>(`${this.UrlBase}/codigos`);
  }

  buscarPorIndice(query: string, indicePais: number, limit = 40): Observable<NominatimLocationModel[]> {
    const params = new HttpParams()
      .set('query', query)
      .set('indicePais', indicePais)
      .set('limit', limit);
    return this.http.get<NominatimLocationModel[]>(`${this.UrlBase}/buscar`, { params });
  }

  buscarPorCodigo(codigo: string, query: string): Observable<NominatimLocationModel[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<NominatimLocationModel[]>(`${this.UrlBase}/buscar/${codigo}`, { params });
  }
}
