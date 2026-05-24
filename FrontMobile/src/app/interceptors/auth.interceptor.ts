import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
/**
 * Interceptor HTTP que agrega el token de autenticación a las solicitudes salientes.
 * No modifica las peticiones dirigidas a los endpoints de autenticación.
 */
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}
  /**
   * Intercepta cada solicitud HTTP y, si existe un token de sesión
   * y la URL no corresponde a rutas de autenticación, clona la solicitud
   * agregando el encabezado Authorization con el token Bearer.
   *
   * @param req solicitud HTTP original
   * @param next manejador que continúa la cadena de interceptores
   * @returns observable con el evento HTTP procesado
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    if (token && !req.url.includes('/auth/')) {
      const reqClone = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      return next.handle(reqClone);
    }

    return next.handle(req);
  }
}
