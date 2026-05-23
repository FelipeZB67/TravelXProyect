import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import {ReservaInternacionalModel} from '../../models/reserva-internacional.model';
import {ReservaNacionalModel} from '../../models/reserva-nacional.model';
import {ReservaInternacionalService} from '../../services/reserva-internacional.service';
import {ReservaNacionalService} from '../../services/reserva-nacional.service';
import {MetodoTransporte} from '../../models/reserva.model';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.html',
  standalone: false,
  styleUrls: ['./inicio.css']
})
export class InicioComponent implements OnInit{
  tabActivo: 'aereo' | 'terrestre' | 'hotel' = 'aereo';

  origenInput = '';
  destinoInput = '';
  mostrarDropdownOrigen = false;
  mostrarDropdownDestino = false;
  cargando = false;


  reservasInternacionales: ReservaInternacionalModel[] = [];
  reservasNacionales: ReservaNacionalModel[] = [];

  ciudades: string[] = [];

  constructor(
    private router: Router,
    private reservaIntService: ReservaInternacionalService,
    private reservaNacService: ReservaNacionalService
  ) {}

  ngOnInit(): void {
    this.cargarReservas();
  }

  cargarReservas(): void {
    this.cargando = true;

    this.reservaIntService.getAll().subscribe({
      next: data => {
        this.reservasInternacionales = data ?? [];
        const origenes = this.reservasInternacionales.map(r => r.ciudadOrigen).filter(Boolean);
        const destinos = this.reservasInternacionales.map(r => r.ciudadDestino).filter(Boolean);
        this.ciudades = [...new Set([...origenes, ...destinos, ...this.ciudades])];
        this.cargando = false;
      },
      error: () => { this.cargando = false; }
    });

    this.reservaNacService.getAll().subscribe({
      next: data => {
        this.reservasNacionales = data ?? [];
        const origenes = this.reservasNacionales.map(r => r.ciudadOrigen).filter(Boolean);
        const destinos = this.reservasNacionales.map(r => r.ciudadDestino).filter(Boolean);
        this.ciudades = [...new Set([...this.ciudades, ...origenes, ...destinos])];
      },
      error: () => {}
    });
  }

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

  get viajesAereos(): any[] {
    return this.reservasInternacionales
      .filter(r => r.metodoTransporte === MetodoTransporte.AEREO)
      .map(r => ({
        nombre: `${r.ciudadOrigen} → ${r.ciudadDestino}`,
        origen: r.ciudadOrigen,
        destino: r.ciudadDestino,
        precio: r.precioTransporte,
        duracion: `${r.fechaInicio} - ${r.fechaFin}`,
        imagen: 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=600&auto=format&fit=crop'
      }));
  }

  get viajesTerrestres(): any[] {
    return this.reservasInternacionales
      .filter(r => r.metodoTransporte === MetodoTransporte.TERRESTRE)
      .map(r => ({
        nombre: `${r.ciudadOrigen} → ${r.ciudadDestino}`,
        origen: r.ciudadOrigen,
        destino: r.ciudadDestino,
        precio: r.precioTransporte,
        duracion: `${r.fechaInicio} - ${r.fechaFin}`,
        imagen: 'https://images.unsplash.com/photo-1559521783-1d1599583485?w=600&auto=format&fit=crop'
      }));
  }

  get hoteles(): any[] {
    const intHoteles = this.reservasInternacionales
      .filter(r => r.hotel)
      .map(r => ({
        nombre: r.hotel,
        ciudad: r.ciudadDestino,
        precio: r.precioHospedaje,
        estrellas: 4,
        imagen: 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop'
      }));

    const nacHoteles = this.reservasNacionales
      .filter(r => r.hotel)
      .map(r => ({
        nombre: r.hotel,
        ciudad: r.ciudadDestino,
        precio: r.precioHospedaje,
        estrellas: 4,
        imagen: 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop'
      }));

    return [...intHoteles, ...nacHoteles];
  }

  get itemsActivos(): any[] {
    if (this.tabActivo === 'aereo') return this.viajesAereos;
    if (this.tabActivo === 'terrestre') return this.viajesTerrestres;
    return this.hoteles;
  }

  cerrarSesion() {
    this.router.navigate(['/login']);
  }

  get heroBackground(): string {
    if (this.tabActivo === 'aereo') return 'url(https://images.unsplash.com/photo-1436491865332-7a61a109cc05?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YXZpb24lMjBkZSUyMHZpYWplfGVufDB8fDB8fHww)';
    if (this.tabActivo === 'terrestre') return 'url(https://alohacamp.com/es/travels/wp-content/uploads/2024/09/montanas-de-espana-ZW.webp)';
    return 'url(https://imagenes2.eltiempo.com/files/image_1200_535/uploads/2024/04/22/6626a322cf16f.jpeg)';
  }

  irACotizacion() {
    this.router.navigate(['/cotizacion']);
  }
}
