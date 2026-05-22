import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PersonaModel } from '../models/persona.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PersonaService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/persona`;

  getAll(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall`);
  }

  getAllAdministradores(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/administradores`);
  }

  getAllUsuarios(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/usuarios`);
  }

  getAllNinguno(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/ninguno`);
  }

  getById(id: number): Observable<PersonaModel> {
    return this.http.get<PersonaModel>(`${this.UrlBase}/getbyid/${id}`);
  }

  getByCorreo(correo: string): Observable<PersonaModel> {
    const params = new HttpParams().set('correo', correo);
    return this.http.get<PersonaModel>(`${this.UrlBase}/getbycorreo`, { params });
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/count`);
  }

  exists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.UrlBase}/exists/${id}`);
  }

  create(persona: PersonaModel): Observable<string> {
    return this.http.post(`${this.UrlBase}/createjson`, persona, { responseType: 'text' });
  }

  update(id: number, persona: PersonaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/updatejson?id=${id}`, persona, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/deletebyid/${id}`, { responseType: 'text' });
  }
}
