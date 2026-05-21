import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.html',
  styleUrls: ['./registro.css']
})
export class RegistroComponent {

  nombre: string = '';
  documento: string = '';
  correo: string = '';
  contrasena: string = '';
  confirmarPassword: string = '';
  tipoUsuario: string = 'USUARIO';
  mostrarPassword: boolean = false;
  mostrarConfirmar: boolean = false;
  cargando: boolean = false;
  mensaje: string = '';
  tipoMensaje: string = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  irABienvenida(): void { this.router.navigate(['/']); }
  irALogin(): void { this.router.navigate(['/login']); }

  registrarse(): void {
    this.nombre = this.nombre.trim();
    this.documento = this.documento.trim();
    this.correo = this.correo.trim();

    if (!this.nombre || !this.documento || !this.correo || !this.contrasena || !this.confirmarPassword) {
      this.mostrarMensaje('Todos los campos son obligatorios', 'error');
      return;
    }

    if (this.nombre.length < 3) {
      this.mostrarMensaje('El nombre debe tener al menos 3 caracteres', 'error');
      return;
    }
    if (this.nombre.length > 60) {
      this.mostrarMensaje('El nombre no puede tener más de 60 caracteres', 'error');
      return;
    }

    const documentoRegex = /^[0-9]+$/;
    if (!documentoRegex.test(this.documento)) {
      this.mostrarMensaje('El documento solo puede contener números', 'error');
      return;
    }
    if (this.documento.length < 6) {
      this.mostrarMensaje('El documento debe tener al menos 6 dígitos', 'error');
      return;
    }
    if (this.documento.length > 12) {
      this.mostrarMensaje('El documento no puede tener más de 12 dígitos', 'error');
      return;
    }

    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!correoRegex.test(this.correo)) {
      this.mostrarMensaje('El correo electrónico no es válido', 'error');
      return;
    }
    if (this.tipoUsuario === 'ADMINISTRADOR' && !this.correo.endsWith('@unbosque.edu.co')) {
      this.mostrarMensaje('El correo ingresado no es válido para registrarse como administrador', 'error');
      return;
    }

    if (this.contrasena.length < 8) {
      this.mostrarMensaje('La contraseña debe tener al menos 8 caracteres', 'error');
      return;
    }
    if (this.contrasena.length > 50) {
      this.mostrarMensaje('La contraseña no puede tener más de 50 caracteres', 'error');
      return;
    }
    if (this.contrasena !== this.confirmarPassword) {
      this.mostrarMensaje('Las contraseñas no coinciden', 'error');
      return;
    }

    this.cargando = true;

    const data = {
      nombre: this.nombre,
      documento: this.documento,
      correo: this.correo,
      contrasena: this.contrasena,
      tipoUsuario: this.tipoUsuario
    };

    this.authService.register(data).subscribe({
      next: (res) => {
        this.cargando = false;
        this.mostrarMensaje(res || 'Cuenta creada. Revisa tu correo.', 'success');
        setTimeout(() => this.router.navigate(['/verificar-correo'], { queryParams: { correo: this.correo } }), 2000);
      },
      error: (err) => {
        this.cargando = false;
        if (err.status === 0) {
          this.mostrarMensaje('No se pudo conectar con el servidor', 'error');
        } else if (err.status === 409) {
          this.mostrarMensaje(err.error || 'El correo o documento ya está registrado', 'error');
        } else if (err.status === 400) {
          this.mostrarMensaje(err.error || 'Datos inválidos', 'error');
        } else if (err.status === 502) {
          this.mostrarMensaje('No se pudo enviar el correo de verificación', 'error');
        } else {
          this.mostrarMensaje(err.error || 'Error al registrarse', 'error');
        }
      }
    });
  }

  mostrarMensaje(msg: string, tipo: string): void {
    this.mensaje = msg;
    this.tipoMensaje = tipo;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.mensaje = '';
      this.cdr.detectChanges();
    }, 5000);
  }
}
