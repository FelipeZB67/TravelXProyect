import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PersonaModel } from '../models/persona.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })

/**
 * Servicio para la gestión de personas.
 * Permite consultar, crear, actualizar y eliminar registros de personas
 * mediante los endpoints del backend.
 */
export class PersonaService {

  private http = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/persona`;

  /** Retorna la lista completa de personas registradas en el sistema. */
  getAll(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall`);
  }

  /** Retorna la lista de personas con rol de administrador. */
  getAllAdministradores(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/administradores`);
  }

  /** Retorna la lista de personas con rol de usuario. */
  getAllUsuarios(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/usuarios`);
  }

  /** Retorna la lista de personas sin rol asignado. */
  getAllNinguno(): Observable<PersonaModel[]> {
    return this.http.get<PersonaModel[]>(`${this.UrlBase}/getall/ninguno`);
  }

  /**
   * Busca una persona por su identificador único.
   *
   * @param id identificador de la persona
   * @returns observable con los datos de la persona encontrada
   */
  getById(id: number): Observable<PersonaModel> {
    return this.http.get<PersonaModel>(`${this.UrlBase}/getbyid/${id}`);
  }

  /**
   * Busca una persona por su correo electrónico.
   *
   * @param correo correo electrónico de la persona
   * @returns observable con los datos de la persona encontrada
   */
  getByCorreo(correo: string): Observable<PersonaModel> {
    const params = new HttpParams().set('correo', correo);
    return this.http.get<PersonaModel>(`${this.UrlBase}/getbycorreo`, { params });
  }

  /** Retorna el total de personas registradas en el sistema. */
  count(): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/count`);
  }

  /**
   * Verifica si existe una persona con el identificador indicado.
   *
   * @param id identificador de la persona
   * @returns observable con true si existe, false si no
   */
  exists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.UrlBase}/exists/${id}`);
  }

  /**
   * Registra una nueva persona en el sistema.
   *
   * @param persona datos de la persona a crear
   * @returns observable con el mensaje de respuesta del servidor
   */
  create(persona: PersonaModel): Observable<string> {
    return this.http.post(`${this.UrlBase}/createjson`, persona, { responseType: 'text' });
  }

  /**
   * Actualiza los datos de una persona existente.
   *
   * @param id identificador de la persona a actualizar
   * @param persona nuevos datos de la persona
   * @returns observable con el mensaje de respuesta del servidor
   */
  update(id: number, persona: PersonaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/updatejson?id=${id}`, persona, { responseType: 'text' });
  }

  /**
   * Elimina una persona del sistema por su identificador.
   *
   * @param id identificador de la persona a eliminar
   * @returns observable con el mensaje de respuesta del servidor
   */
  delete(id: number): Observable<string> {
    return this.http.delete(`${this.UrlBase}/deletebyid/${id}`, { responseType: 'text' });
  }

  updateMiCuenta(persona: PersonaModel): Observable<string> {
    return this.http.put(`${this.UrlBase}/mi-cuenta`, persona, { responseType: 'text' });
  }

}
