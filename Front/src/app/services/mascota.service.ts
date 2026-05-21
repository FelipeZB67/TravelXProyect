import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MascotaModel } from '../models/mascota.model';

@Injectable({ providedIn: 'root' })
export class MascotaService {

  private http = inject(HttpClient);
  private UrlBase = `http://localhost:8080/api/mascotas`;

  getAll(): Observable<MascotaModel[]> {
    return this.http.get<MascotaModel[]>(this.UrlBase);
  }

  create(mascota: MascotaModel): Observable<string> {
    return this.http.post(this.UrlBase, mascota, { responseType: 'text' });
  }

  update(id: number, mascota: MascotaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/${id}`, mascota, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/${id}`, { responseType: 'text' });
  }
}
