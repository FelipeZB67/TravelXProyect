import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TravelSearchRequestModel, TravelSearchResponseModel } from '../models/travel-search.model';

@Injectable({ providedIn: 'root' })

/**
 * Servicio para la búsqueda de opciones de viaje.
 * Permite consultar vuelos, transporte terrestre y hospedaje
 * mediante los endpoints del backend.
 */
export class TravelSearchService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/travel-search`;

  /**
   * Busca todas las opciones de viaje disponibles según los parámetros indicados.
   *
   * @param request parámetros de búsqueda
   * @returns observable con las opciones de viaje encontradas
   */
  buscarTodo(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/searchjson`, request);
  }

  /**
   * Busca opciones de transporte según los parámetros indicados.
   *
   * @param request parámetros de búsqueda
   * @returns observable con las opciones de transporte encontradas
   */
  buscarTransporte(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/transportjson`, request);
  }

  /**
   * Busca opciones de hospedaje según los parámetros indicados.
   *
   * @param request parámetros de búsqueda
   * @returns observable con las opciones de hospedaje encontradas
   */
  buscarHospedaje(request: TravelSearchRequestModel): Observable<TravelSearchResponseModel> {
    return this.http.post<TravelSearchResponseModel>(`${this.urlBase}/lodgingjson`, request);
  }
}
