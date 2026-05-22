import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MascotaModel } from '../models/mascota.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MascotaService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/mascota`;

  getAll(): Observable<MascotaModel[]> {
    return this.http.get<MascotaModel[]>(`${this.UrlBase}/getall`);
  }

  getById(id: number): Observable<MascotaModel> {
    return this.http.get<MascotaModel>(`${this.UrlBase}/getbyid/${id}`);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/count`);
  }

  exists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.UrlBase}/exists/${id}`);
  }

  create(mascota: MascotaModel): Observable<string> {
    return this.http.post(`${this.UrlBase}/createjson`, mascota, { responseType: 'text' });
  }

  update(id: number, mascota: MascotaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/updatejson?id=${id}`, mascota, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/deletebyid/${id}`, { responseType: 'text' });
  }
}
