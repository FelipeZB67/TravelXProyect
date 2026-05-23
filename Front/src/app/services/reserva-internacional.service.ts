import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaInternacionalModel } from '../models/reserva-internacional.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ReservaInternacionalService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/reservainternacional`;

  getAll(): Observable<ReservaInternacionalModel[]> {
    return this.http.get<ReservaInternacionalModel[]>(`${this.UrlBase}/getall`);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/count`);
  }

  exists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.UrlBase}/exists/${id}`);
  }

  create(reserva: ReservaInternacionalModel): Observable<string> {
    return this.http.post(`${this.UrlBase}/createjson`, reserva, { responseType: 'text' });
  }

  update(id: number, reserva: ReservaInternacionalModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/updatejson?id=${id}`, reserva, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/deletebyid/${id}`, { responseType: 'text' });
  }
}
