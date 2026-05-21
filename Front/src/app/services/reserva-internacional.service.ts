import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaInternacionalModel } from '../models/reserva-internacional.model';

@Injectable({ providedIn: 'root' })
export class ReservaInternacionalService {

  private http = inject(HttpClient);
  private UrlBase = `http://localhost:8080/api/reservas/internacional`;

  getAll(): Observable<ReservaInternacionalModel[]> {
    return this.http.get<ReservaInternacionalModel[]>(this.UrlBase);
  }

  getById(id: number): Observable<ReservaInternacionalModel> {
    return this.http.get<ReservaInternacionalModel>(`${this.UrlBase}/${id}`);
  }

  create(reserva: ReservaInternacionalModel): Observable<string> {
    return this.http.post(this.UrlBase, reserva, { responseType: 'text' });
  }

  update(id: number, reserva: ReservaInternacionalModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/${id}`, reserva, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/${id}`, { responseType: 'text' });
  }
}
