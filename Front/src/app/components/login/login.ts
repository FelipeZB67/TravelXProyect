import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
  standalone: false
})
export class LoginComponent {

  email = '';
  password = '';
  mostrarPassword = false;

  constructor(private router: Router) {}

  irARegistro(): void {
    this.router.navigate(['/registro']);
  }

  irABienvenida(): void {
    this.router.navigate(['/']);
  }

  togglePassword(): void {
    this.mostrarPassword = !this.mostrarPassword;
  }

  iniciarSesion(): void {
    console.log('Login:', this.email, this.password);
  }
}
