import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');
  const tipoUsuario = localStorage.getItem('tipoUsuario');

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  const rolesPermitidos = route.data['roles'] as string[];

  if (rolesPermitidos && !rolesPermitidos.includes(tipoUsuario ?? '')) {
    router.navigate(['/inicio']);
    return false;
  }

  return true;
};
