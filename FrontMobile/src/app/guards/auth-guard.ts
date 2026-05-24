import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard funcional que protege las rutas de la aplicación.
 * Verifica que el usuario tenga un token válido y, si la ruta
 * requiere un rol específico, comprueba que el tipo de usuario coincida.
 * Redirige a login si no hay token, o a inicio si el rol no es permitido.
 */
export const authGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  const token = authService.getToken();
  const tipoUsuario = authService.getTipoUsuario();

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const rolesPermitidos = route.data['roles'] as string[] | undefined;

  if (rolesPermitidos && !rolesPermitidos.includes(tipoUsuario ?? '')) {
    router.navigate(['/inicio']);
    return false;
  }

  return true;
};
