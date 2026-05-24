import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { BienvenidaComponent } from './components/bienvenida/bienvenida';
import { LoginComponent } from './components/login/login';
import { RegistroComponent } from './components/registro/registro';
import { InicioComponent } from './components/inicio/inicio';
import { AdminComponent } from './components/admin/admin';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { VerificarCorreoComponent } from './components/verificar-correo/verificar-correo';
import { CotizacionComponent } from './components/cotizacion/cotizacion.component';
import { AuthInterceptor } from './interceptors/auth.interceptor';
@NgModule({
  declarations: [
    App,
    BienvenidaComponent,
    LoginComponent,
    RegistroComponent,
    InicioComponent,
    AdminComponent,
    VerificarCorreoComponent,
    CotizacionComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    RouterModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [App],
})
/**
 * Módulo raíz de la aplicación.
 * Declara todos los componentes, importa los módulos necesarios
 * y registra el interceptor de autenticación HTTP.
 */
export class AppModule {}
