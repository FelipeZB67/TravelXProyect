import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verificar-correo',
  standalone: false,
  templateUrl: './verificar-correo.html',
  styleUrls: ['./verificar-correo.css']
})
export class VerificarCorreoComponent implements OnInit {
  correo = '';
  codigo = '';
  cargando = false;
  error = '';
  exito = '';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.correo = this.route.snapshot.queryParams['correo'] || '';

    if (!this.correo) {
      this.error = 'No se encontró el correo a verificar. Vuelve a iniciar el registro.';
    }
  }

  verificar(): void {
    this.codigo = this.codigo.trim();
    this.error = '';
    this.exito = '';

    if (!this.correo) {
      this.error = 'No se encontró el correo a verificar. Vuelve a iniciar el registro.';
      return;
    }

    if (!this.codigo) {
      this.error = 'Ingresa el código de verificación.';
      return;
    }

    if (!/^[0-9]+$/.test(this.codigo)) {
      this.error = 'El código solo puede contener números.';
      return;
    }

    if (this.codigo.length !== 6) {
      this.error = 'El código debe tener 6 dígitos.';
      return;
    }

    this.cargando = true;

    this.authService.verifyEmail(this.correo, this.codigo).subscribe({
      next: () => {
        this.cargando = false;
        this.exito = '¡Correo verificado! Redirigiendo...';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: err => {
        this.error = this.obtenerMensajeError(err);
        this.cargando = false;
      }
    });
  }

  private obtenerMensajeError(err: any): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'El código ingresado no es válido.';
    if (err?.status === 401) return 'No tienes autorización para verificar este correo.';
    if (err?.status === 403) return mensajeBack || 'No tienes permisos para realizar esta acción.';
    if (err?.status === 404) return mensajeBack || 'No se encontró una cuenta asociada a ese correo.';
    if (err?.status === 409) return mensajeBack || 'Este correo ya fue verificado.';
    if (err?.status >= 500) return 'El servidor tuvo un problema. Intenta de nuevo en unos minutos.';

    return mensajeBack || 'Código inválido. Intenta de nuevo.';
  }
}
