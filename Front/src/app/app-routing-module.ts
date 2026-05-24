import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BienvenidaComponent } from './components/bienvenida/bienvenida';
import { LoginComponent } from './components/login/login';
import { RegistroComponent } from './components/registro/registro';
import { InicioComponent } from './components/inicio/inicio';
import { AdminComponent } from './components/admin/admin';
import { CotizacionComponent } from './components/cotizacion/cotizacion';
import { authGuard } from './guards/auth-guard';
import { VerificarCorreoComponent } from './components/verificar-correo/verificar-correo';

/**
 * Definición de rutas de la aplicación.
 * Algunas rutas están protegidas por el guard de autenticación
 * y una de ellas restringe el acceso según el rol del usuario.
 */
const routes: Routes = [
  { path: '', component: BienvenidaComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'verificar-correo', component: VerificarCorreoComponent },
  { path: 'inicio', component: InicioComponent, canActivate: [authGuard] },
  { path: 'cotizacion', component: CotizacionComponent, canActivate: [authGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRADOR'] } },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
/**
 * Módulo de enrutamiento principal.
 * Configura las rutas de la aplicación y las expone para su uso en otros módulos.
 */
export class AppRoutingModule {}
