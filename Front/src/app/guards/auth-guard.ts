import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

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
