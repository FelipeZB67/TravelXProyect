import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ViajeroModel } from '../models/viajero.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ViajeroService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/viajero`;

  getAll(): Observable<ViajeroModel[]> {
    return this.http.get<ViajeroModel[]>(`${this.UrlBase}/getall`);
  }

  getById(id: number): Observable<ViajeroModel> {
    return this.http.get<ViajeroModel>(`${this.UrlBase}/getbyid/${id}`);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/count`);
  }

  exists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.UrlBase}/exists/${id}`);
  }

  existsPasaporte(pasaporte: string): Observable<boolean> {
    const params = new HttpParams().set('pasaporte', pasaporte);
    return this.http.get<boolean>(`${this.UrlBase}/exists/pasaporte`, { params });
  }

  create(viajero: ViajeroModel): Observable<string> {
    return this.http.post(`${this.UrlBase}/createjson`, viajero, { responseType: 'text' });
  }

  update(id: number, viajero: ViajeroModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/updatejson?id=${id}`, viajero, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/deletebyid/${id}`, { responseType: 'text' });
  }
}
