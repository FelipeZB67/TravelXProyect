import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BienvenidaComponent } from './components/bienvenida/bienvenida';
import { LoginComponent } from './components/login/login';
import { RegistroComponent } from './components/registro/registro';
import {InicioComponent} from './components/inicio/inicio';
import {AdminComponent} from './components/admin/admin';
import {authGuard} from './guards/auth-guard';
import {VerificarCorreoComponent} from './components/verificar-correo/verificar-correo';

const routes: Routes = [
  { path: '', component: BienvenidaComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'verificar-correo', component: VerificarCorreoComponent },
  { path: 'inicio', component: InicioComponent, canActivate: [authGuard] },
  { path: 'admin', component: AdminComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRADOR'] } },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
