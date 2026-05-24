import { Component, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.html',
  styleUrls: ['./registro.css']
})

/**
 * Componente de registro de usuarios.
 * Gestiona el formulario de creación de cuenta, aplica validaciones
 * y redirige al usuario a la verificación de correo tras registrarse exitosamente.
 */
export class RegistroComponent {
  nombre = '';
  documento = '';
  correo = '';
  contrasena = '';
  confirmarPassword = '';
  tipoUsuario = 'USUARIO';
  mostrarPassword = false;
  mostrarConfirmar = false;
  cargando = false;
  mensaje = '';
  tipoMensaje = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  /** Navega hacia la vista de bienvenida. */
  irABienvenida(): void {
    this.router.navigate(['/']);
  }

  /** Navega hacia la vista de inicio de sesión. */
  irALogin(): void {
    this.router.navigate(['/login']);
  }

  /**
   * Valida el formulario y envía los datos de registro al servicio de autenticación.
   * Si el registro es exitoso, redirige a la vista de verificación de correo.
   */
  registrarse(): void {
    this.nombre = this.nombre.trim();
    this.documento = this.documento.trim();
    this.correo = this.correo.trim().toLowerCase();
    this.mensaje = '';
    this.tipoMensaje = '';

    const validacion = this.validarFormulario();

    if (validacion) {
      this.mostrarMensaje(validacion, 'error');
      return;
    }

    this.cargando = true;
    this.cdr.detectChanges();

    const data = {
      nombre: this.nombre,
      documento: this.documento,
      correo: this.correo,
      contrasena: this.contrasena,
      tipoUsuario: this.tipoUsuario
    };

    this.authService.register(data).subscribe({
      next: res => {
        this.cargando = false;
        this.mostrarMensaje(res || 'Cuenta creada. Revisa tu correo para verificarla.', 'success');
        setTimeout(() => this.router.navigate(['/verificar-correo'], { queryParams: { correo: this.correo } }), 2000);
      },
      error: err => {
        this.cargando = false;
        this.mostrarMensaje(this.obtenerMensajeError(err), 'error');
      }
    });
  }

  /**
   * Muestra un mensaje temporal de éxito o error en la vista.
   *
   * @param msg texto del mensaje a mostrar
   * @param tipo tipo de mensaje, puede ser 'success' o 'error'
   */
  mostrarMensaje(msg: string, tipo: string): void {
    this.mensaje = msg;
    this.tipoMensaje = tipo;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.mensaje = '';
      this.cdr.detectChanges();
    }, 5000);
  }

  private validarFormulario(): string {
    const nombreRegex = /^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ]+(?: [A-Za-zÁÉÍÓÚÜÑáéíóúüñ]+)*$/;
    const documentoRegex = /^[0-9]+$/;

    if (!this.nombre || !this.documento || !this.correo || !this.contrasena || !this.confirmarPassword) {
      return 'Todos los campos son obligatorios.';
    }

    if (!nombreRegex.test(this.nombre)) {
      return 'El nombre solo puede contener letras y espacios. No uses números ni caracteres especiales.';
    }

    if (this.nombre.length < 3) {
      return 'El nombre debe tener al menos 3 caracteres.';
    }

    if (this.nombre.length > 60) {
      return 'El nombre no puede tener más de 60 caracteres.';
    }

    if (!documentoRegex.test(this.documento)) {
      return 'El documento solo puede contener números. No uses signos, espacios ni valores negativos.';
    }

    if (this.documento.length < 6) {
      return 'El documento debe tener al menos 6 dígitos.';
    }

    if (this.documento.length > 12) {
      return 'El documento no puede tener más de 12 dígitos.';
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.correo)) {
      return 'El correo electrónico no es válido.';
    }

    if (this.tipoUsuario === 'ADMINISTRADOR' && !this.correo.endsWith('@unbosque.edu.co')) {
      return 'El correo ingresado no es válido para registrarse como administrador.';
    }

    if (this.contrasena.length < 8) {
      return 'La contraseña debe tener al menos 8 caracteres.';
    }

    if (this.contrasena.length > 50) {
      return 'La contraseña no puede tener más de 50 caracteres.';
    }

    if (this.contrasena !== this.confirmarPassword) {
      return 'Las contraseñas no coinciden.';
    }

    return '';
  }

  private obtenerMensajeError(err: any): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'Los datos ingresados no son válidos.';
    if (err?.status === 401) return 'No tienes autorización para realizar este registro.';
    if (err?.status === 403) return mensajeBack || 'No tienes permisos para realizar esta acción.';
    if (err?.status === 409) return mensajeBack || 'El nombre, correo o documento ya está registrado.';
    if (err?.status === 502) return 'La cuenta se creó, pero no se pudo enviar el correo de verificación.';
    if (err?.status >= 500) return 'El servidor tuvo un problema. Intenta de nuevo en unos minutos.';

    return mensajeBack || 'No se pudo completar el registro.';
  }
}
