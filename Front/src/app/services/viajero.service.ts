import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ViajeroModel } from '../models/viajero.model';
import {environment} from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ViajeroService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/api/viajeros`;

  getAll(): Observable<ViajeroModel[]> {
    return this.http.get<ViajeroModel[]>(this.UrlBase);
  }

  create(viajero: ViajeroModel): Observable<string> {
    return this.http.post(this.UrlBase, viajero, { responseType: 'text' });
  }

  update(id: number, viajero: ViajeroModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/${id}`, viajero, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/${id}`, { responseType: 'text' });
  }
}
