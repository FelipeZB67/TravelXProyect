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

  ngOnInit() {
    this.correo = this.route.snapshot.queryParams['correo'] || '';
  }

  verificar() {
    if (!this.codigo) {
      this.error = 'Ingresa el código de verificación.';
      return;
    }
    this.cargando = true;
    this.error = '';

    this.authService.verifyEmail(this.correo, this.codigo).subscribe({
      next: () => {
        this.exito = '¡Correo verificado! Redirigiendo...';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.error = err.error || 'Código inválido. Intenta de nuevo.';
        this.cargando = false;
      }
    });
  }
}
