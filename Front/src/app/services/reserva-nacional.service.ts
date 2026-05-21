import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaNacionalModel } from '../models/reserva-nacional.model';

@Injectable({ providedIn: 'root' })
export class ReservaNacionalService {

  private http = inject(HttpClient);
  private UrlBase = `http://localhost:8080/api/reservas/nacional`;

  getAll(): Observable<ReservaNacionalModel[]> {
    return this.http.get<ReservaNacionalModel[]>(this.UrlBase);
  }

  getById(id: number): Observable<ReservaNacionalModel> {
    return this.http.get<ReservaNacionalModel>(`${this.UrlBase}/${id}`);
  }

  create(reserva: ReservaNacionalModel): Observable<string> {
    return this.http.post(this.UrlBase, reserva, { responseType: 'text' });
  }

  update(id: number, reserva: ReservaNacionalModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/${id}`, reserva, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/${id}`, { responseType: 'text' });
  }
}
