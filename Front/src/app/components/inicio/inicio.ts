import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.html',
  standalone: false,
  styleUrls: ['./inicio.css']
})
export class InicioComponent {
  tabActivo: 'aereo' | 'terrestre' | 'hotel' = 'aereo';

  origenInput = '';
  destinoInput = '';
  mostrarDropdownOrigen = false;
  mostrarDropdownDestino = false;

  ciudades: string[] = [
    'Bogotá', 'Medellín', 'Cali', 'Cartagena', 'Barranquilla',
    'Bucaramanga', 'Santa Marta', 'Pereira', 'Manizales', 'Leticia',
    'París', 'Londres', 'Nueva York', 'Miami', 'Tokio', 'Cancún', 'Lima', 'Buenos Aires'
  ];

  get origenesFiltrados(): string[] {
    if (!this.origenInput) return [];
    return this.ciudades.filter(c => c.toLowerCase().includes(this.origenInput.toLowerCase()));
  }

  get destinosFiltrados(): string[] {
    if (!this.destinoInput) return [];
    return this.ciudades.filter(c => c.toLowerCase().includes(this.destinoInput.toLowerCase()));
  }

  seleccionarOrigen(ciudad: string) {
    this.origenInput = ciudad;
    this.mostrarDropdownOrigen = false;
  }

  seleccionarDestino(ciudad: string) {
    this.destinoInput = ciudad;
    this.mostrarDropdownDestino = false;
  }

  viajesAereos: any[] = [
    { nombre: 'Vuelo a París', origen: 'Bogotá', destino: 'París', precio: 1200, duracion: '12h', imagen: 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=600&auto=format&fit=crop' },
    { nombre: 'Vuelo a Nueva York', origen: 'Medellín', destino: 'Nueva York', precio: 980, duracion: '8h', imagen: 'https://images.unsplash.com/photo-1485871981521-5b1fd3805eee?w=600&auto=format&fit=crop' },
    { nombre: 'Vuelo a Tokio', origen: 'Bogotá', destino: 'Tokio', precio: 1800, duracion: '22h', imagen: 'https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=600&auto=format&fit=crop' },
    { nombre: 'Vuelo a Londres', origen: 'Cali', destino: 'Londres', precio: 1100, duracion: '11h', imagen: 'https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?w=600&auto=format&fit=crop' },
  ];

  viajesTerrestres: any[] = [
    { nombre: 'Ruta Eje Cafetero', origen: 'Bogotá', destino: 'Salento', precio: 180, duracion: '2 días', imagen: 'https://images.unsplash.com/photo-1559521783-1d1599583485?w=600&auto=format&fit=crop' },
    { nombre: 'Costa Atlántica', origen: 'Barranquilla', destino: 'Cartagena', precio: 95, duracion: '1 día', imagen: 'https://images.unsplash.com/photo-1533106418989-88406c7cc8ca?w=600&auto=format&fit=crop' },
    { nombre: 'Travesía por los Andes', origen: 'Quito', destino: 'Lima', precio: 420, duracion: '4 días', imagen: 'https://images.unsplash.com/photo-1526392060635-9d6019884377?w=600&auto=format&fit=crop' },
    { nombre: 'Tour Villa de Leyva', origen: 'Bogotá', destino: 'Villa de Leyva', precio: 75, duracion: '1 día', imagen: 'https://images.unsplash.com/photo-1518684079-3c830dcef090?w=600&auto=format&fit=crop' },
  ];

  hoteles: any[] = [
    { nombre: 'Hotel Estelar Bogotá', ciudad: 'Bogotá', precio: 180, estrellas: 5, imagen: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop' },
    { nombre: 'Dann Carlton Medellín', ciudad: 'Medellín', precio: 140, estrellas: 5, imagen: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop' },
    { nombre: 'Hotel Santa Clara', ciudad: 'Cartagena', precio: 220, estrellas: 5, imagen: 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600&auto=format&fit=crop' },
    { nombre: 'GHL Bucaramanga', ciudad: 'Bucaramanga', precio: 95, estrellas: 4, imagen: 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600&auto=format&fit=crop' },
  ];

  get itemsActivos(): any[] {
    if (this.tabActivo === 'aereo') return this.viajesAereos;
    if (this.tabActivo === 'terrestre') return this.viajesTerrestres;
    return this.hoteles;
  }

  constructor(private router: Router) {}

  cerrarSesion() {
    this.router.navigate(['/login']);
  }

  get heroBackground(): string {
    if (this.tabActivo === 'aereo') return 'url(https://images.unsplash.com/photo-1436491865332-7a61a109cc05?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YXZpb24lMjBkZSUyMHZpYWplfGVufDB8fDB8fHww)';
    if (this.tabActivo === 'terrestre') return 'url(https://alohacamp.com/es/travels/wp-content/uploads/2024/09/montanas-de-espana-ZW.webp)';
    return 'url(https://imagenes2.eltiempo.com/files/image_1200_535/uploads/2024/04/22/6626a322cf16f.jpeg)';
  }
}
