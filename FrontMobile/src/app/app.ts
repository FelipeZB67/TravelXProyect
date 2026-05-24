import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css'
})
/**
 * Componente raíz de la aplicación.
 * Actúa como punto de entrada principal y contiene el enrutador de vistas.
 */
export class App {
  protected readonly title = signal('Front');
}
