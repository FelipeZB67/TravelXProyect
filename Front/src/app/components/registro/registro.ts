import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.html',
  styleUrls: ['./registro.css'],
  standalone: false
})
export class RegistroComponent {

  nombre = '';
  email = '';
  password = '';
  confirmarPassword = '';
  mostrarPassword = false;
  mostrarConfirmar = false;

  constructor(private router: Router) {}

  irALogin(): void {
    this.router.navigate(['/login']);
  }

  irABienvenida(): void {
    this.router.navigate(['/']);
  }

  togglePassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  toggleConfirmar(): void {
    this.mostrarConfirmar = !this.mostrarConfirmar;
  }

  registrarse(): void {
    console.log('Registro:', this.nombre, this.email, this.password);
  }
}
