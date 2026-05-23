import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  correo = '';
  contrasena = '';
  mostrarPassword = false;
  cargando = false;
  error = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  iniciarSesion(): void {
    this.correo = this.correo.trim();
    this.error = '';

    if (!this.correo || !this.contrasena) {
      this.error = 'Por favor completa todos los campos.';
      this.cdr.detectChanges();
      return;
    }

    if (!this.correoValido(this.correo)) {
      this.error = 'Ingresa un correo electrónico válido.';
      this.cdr.detectChanges();
      return;
    }

    this.cargando = true;
    this.cdr.detectChanges();

    this.authService.login(this.correo, this.contrasena).subscribe({
      next: res => {
        this.authService.guardarSesion(res);

        if (res.tipoUsuario === 'ADMINISTRADOR') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/inicio']);
        }
      },
      error: err => {
        this.error = this.obtenerMensajeError(err);
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  togglePassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  irABienvenida(): void {
    this.router.navigate(['/']);
  }

  irARegistro(): void {
    this.router.navigate(['/registro']);
  }

  private correoValido(correo: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo);
  }

  private obtenerMensajeError(err: any): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'Los datos ingresados no son válidos.';
    if (err?.status === 401) return mensajeBack || 'Correo o contraseña incorrectos.';
    if (err?.status === 403) return mensajeBack || 'Tu cuenta no tiene permisos para ingresar.';
    if (err?.status === 404) return mensajeBack || 'No existe una cuenta con ese correo.';
    if (err?.status >= 500) return 'El servidor tuvo un problema. Intenta de nuevo en unos minutos.';

    return mensajeBack || 'No se pudo iniciar sesión.';
  }
}
