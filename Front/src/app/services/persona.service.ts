import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PersonaModel } from '../models/persona.model';
import {environment} from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PersonaService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/api/personas`;

  getAll(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(this.UrlBase);
  }

  getById(id: number): Observable<PersonaModel> {
    return this.http.get<PersonaModel>(`${this.UrlBase}/${id}`);
  }

  create(persona: PersonaModel): Observable<string> {
    return this.http.post(this.UrlBase, persona, { responseType: 'text' });
  }

  update(id: number, persona: PersonaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/${id}`, persona, { responseType: 'text' });
  }

  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/${id}`, { responseType: 'text' });
  }
}
