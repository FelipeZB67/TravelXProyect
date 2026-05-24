import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })

/**
 * Servicio de autenticación.
 * Gestiona el inicio de sesión, registro y verificación de correo,
 * así como el almacenamiento y recuperación de los datos de sesión del usuario.
 */
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  /**
   * Envía las credenciales del usuario al backend para iniciar sesión.
   *
   * @param correo correo electrónico del usuario
   * @param contrasena contraseña del usuario
   * @returns observable con la respuesta de autenticación
   */
  login(correo: string, contrasena: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { correo, contrasena });
  }

  /**
   * Envía los datos de registro de un nuevo usuario al backend.
   *
   * @param data objeto con los datos del usuario a registrar
   * @returns observable con el mensaje de respuesta del servidor
   */
  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data, { responseType: 'text' });
  }

  /**
   * Envía el código de verificación al backend para activar la cuenta del usuario.
   *
   * @param correo correo electrónico a verificar
   * @param codigo código de verificación recibido por correo
   * @returns observable con el mensaje de respuesta del servidor
   */
  verifyEmail(correo: string, codigo: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verify`, { correo, codigo }, { responseType: 'text' });
  }

  /**
   * Almacena los datos de sesión del usuario en el almacenamiento local.
   *
   * @param data objeto con token, tipo de usuario, nombre y correo
   */
  guardarSesion(data: any): void {
    localStorage.setItem('token', data.token);
    localStorage.setItem('tipoUsuario', data.tipoUsuario);
    localStorage.setItem('nombre', data.nombre);
    localStorage.setItem('correo', data.correo);
  }

  /**
   * Actualiza el nombre del usuario almacenado en la sesión local.
   *
   * @param nombre nuevo nombre del usuario
   */
  actualizarDatosSesion(nombre: string): void {
    localStorage.setItem('nombre', nombre);
  }

  /** Retorna el token de autenticación almacenado en la sesión local. */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /** Retorna el tipo de usuario almacenado en la sesión local. */
  getTipoUsuario(): string | null {
    return localStorage.getItem('tipoUsuario');
  }

  /** Retorna el nombre del usuario almacenado en la sesión local. */
  getNombre(): string | null {
    return localStorage.getItem('nombre');
  }

  /** Retorna el correo del usuario almacenado en la sesión local. */
  getCorreo(): string | null {
    return localStorage.getItem('correo');
  }

  /** Indica si el usuario tiene una sesión activa verificando la existencia del token. */
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  /** Indica si el usuario autenticado tiene rol de administrador. */
  isAdmin(): boolean {
    return this.getTipoUsuario() === 'ADMINISTRADOR';
  }

  /** Cierra la sesión del usuario limpiando el almacenamiento local. */
  logout(): void {
    localStorage.clear();
  }
}
