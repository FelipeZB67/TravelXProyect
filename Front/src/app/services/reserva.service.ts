import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TravelOptionModel } from '../models/travel-option.model';

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/reservas`;

  agregar(option: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.post<TravelOptionModel>(`${this.urlBase}/agregar`, option);
  }

  misReservas(): Observable<TravelOptionModel[]> {
    return this.http.get<TravelOptionModel[]>(`${this.urlBase}/mis-reservas`);
  }

  todas(): Observable<TravelOptionModel[]> {
    return this.http.get<TravelOptionModel[]>(`${this.urlBase}/todas`);
  }

  crearAdmin(reserva: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.post<TravelOptionModel>(`${this.urlBase}/admin/crear`, reserva);
  }

  actualizarAdmin(id: number, reserva: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.put<TravelOptionModel>(`${this.urlBase}/admin/actualizar/${id}`, reserva);
  }

  eliminarAdmin(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/admin/eliminar/${id}`);
  }

  imprimir(id: number): Observable<string> {
    return this.http.get(`${this.urlBase}/${id}/imprimir`, { responseType: 'text' });
  }
}
