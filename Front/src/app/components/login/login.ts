import { Component } from '@angular/core';
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

  constructor(private authService: AuthService, private router: Router) {}

  iniciarSesion() {
    if (!this.correo || !this.contrasena) {
      this.error = 'Por favor completa todos los campos.';
      return;
    }
    this.cargando = true;
    this.error = '';

    this.authService.login(this.correo, this.contrasena).subscribe({
      next: (res) => {
        this.authService.guardarSesion(res);
        if (res.tipoUsuario === 'ADMINISTRADOR') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/inicio']);
        }
      },
      error: () => {
        this.error = 'Correo o contraseña inválidos.';
        this.cargando = false;
      }
    });
  }

  togglePassword() { this.mostrarPassword = !this.mostrarPassword; }
  irABienvenida() { this.router.navigate(['/']); }
  irARegistro() { this.router.navigate(['/registro']); }
}
