import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

/** Representa el resultado de una búsqueda de aeropuerto por ciudad y código de país. */
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

/**
 * Servicio para la búsqueda de aeropuertos.
 * Consulta el endpoint del backend que interactúa con Google Flights
 * para encontrar el código IATA de un aeropuerto dado una ciudad y un país.
 */
export class AirportService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/google-flights-airport`;

  /**
   * Busca el aeropuerto correspondiente a una ciudad y código de país.
   *
   * @param query nombre de la ciudad a buscar
   * @param countryCode código ISO del país
   * @returns observable con el resultado de la búsqueda del aeropuerto
   */
  buscarAeropuerto(query: string, countryCode: string): Observable<AirportCodeModel> {
    const params = new HttpParams()
      .set('query', query)
      .set('countryCode', countryCode);

    return this.http.get<AirportCodeModel>(`${this.urlBase}/search`, { params });
  }
}
