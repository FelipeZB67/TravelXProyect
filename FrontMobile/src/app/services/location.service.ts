import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ResolvedLocationModel } from '../models/travel-search.model';

@Injectable({ providedIn: 'root' })

/**
 * Servicio para la validación de ubicaciones geográficas.
 * Consulta el backend que utiliza la API de Nominatim para verificar
 * que una ciudad pertenece al país indicado.
 */
export class LocationService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/nominatim`;

  /**
   * Valida si una ciudad existe dentro del país indicado.
   *
   * @param city nombre de la ciudad a validar
   * @param country nombre del país
   * @returns observable con la ubicación resuelta por el servidor
   */
  validarCiudadPais(city: string, country: string): Observable<ResolvedLocationModel> {
    const params = new HttpParams()
      .set('city', city)
      .set('country', country);

    return this.http.get<ResolvedLocationModel>(`${this.urlBase}/search`, { params });
  }
}
