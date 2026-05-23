import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(correo: string, contrasena: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { correo, contrasena });
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data, { responseType: 'text' });
  }

  verifyEmail(correo: string, codigo: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/verify`, { correo, codigo }, { responseType: 'text' });
  }

  guardarSesion(data: any): void {
    localStorage.setItem('token', data.token);
    localStorage.setItem('tipoUsuario', data.tipoUsuario);
    localStorage.setItem('nombre', data.nombre);
    localStorage.setItem('correo', data.correo);
  }

  actualizarDatosSesion(nombre: string): void {
    localStorage.setItem('nombre', nombre);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getTipoUsuario(): string | null {
    return localStorage.getItem('tipoUsuario');
  }

  getNombre(): string | null {
    return localStorage.getItem('nombre');
  }

  getCorreo(): string | null {
    return localStorage.getItem('correo');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.getTipoUsuario() === 'ADMINISTRADOR';
  }

  logout(): void {
    localStorage.clear();
  }
}
