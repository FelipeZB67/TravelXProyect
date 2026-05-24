import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-bienvenida',
  templateUrl: './bienvenida.html',
  styleUrls: ['./bienvenida.css'],
  standalone: false
})
/**
 * Componente de bienvenida de la aplicación.
 * Presenta la pantalla inicial con opciones para iniciar sesión,
 * registrarse o desplazarse hacia la sección de características.
 */
export class BienvenidaComponent {

  constructor(private router: Router) {}

  /** Navega hacia la vista de inicio de sesión. */
  irALogin(): void {
    this.router.navigate(['/login']);
  }

  /** Navega hacia la vista de registro de usuarios. */
  irARegistro(): void {
    this.router.navigate(['/registro']);
  }

  /** Desplaza la vista suavemente hacia la sección de características de la página. */
  scrollAFeatures(): void {
    const el = document.getElementById('features');
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  }
}
