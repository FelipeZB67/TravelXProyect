import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaInternacionalModel } from '../../models/reserva-internacional.model';
import { ReservaNacionalModel } from '../../models/reserva-nacional.model';
import { ReservaInternacionalService } from '../../services/reserva-internacional.service';
import { ReservaNacionalService } from '../../services/reserva-nacional.service';
import { QuotePdfService } from '../../services/quote-pdf.service';
import {MetodoTransporte} from '../../models/reserva.model';

@Component({
  selector: 'app-cotizacion',
  standalone: false,
  templateUrl: './cotizacion.html',
  styleUrl: './cotizacion.css'
})
export class Cotizacion implements OnInit {

  tabActivo: 'internacional' | 'nacional' = 'internacional';

  reservasInternacionales: ReservaInternacionalModel[] = [];
  reservasNacionales:      ReservaNacionalModel[]      = [];

  cargando = false;
  error    = '';

  constructor(
    private router:                     Router,
    private reservaIntService:          ReservaInternacionalService,
    private reservaNacService:          ReservaNacionalService,
    private quotePdfService:            QuotePdfService
  ) {}

  ngOnInit(): void {
    this.reservasInternacionales = [
      {
        id: 1,
        personaId: 1,
        metodoTransporte: MetodoTransporte.AEREO,
        fechaInicio: '2025-06-15',
        fechaFin: '2025-06-22',
        ciudadOrigen: 'Bogotá',
        ciudadDestino: 'Ciudad de México',
        precioTransporte: 480,
        hotel: 'Hotel Marquis Reforma',
        precioHospedaje: 840,
        paisOrigen: 'Colombia',
        paisDestino: 'México',
        requiereVisa: false
      },
      {
        id: 2,
        personaId: 1,
        metodoTransporte: MetodoTransporte.AEREO,
        fechaInicio: '2025-08-10',
        fechaFin: '2025-08-20',
        ciudadOrigen: 'Medellín',
        ciudadDestino: 'París',
        precioTransporte: 1200,
        hotel: 'Hotel Le Marais',
        precioHospedaje: 1800,
        paisOrigen: 'Colombia',
        paisDestino: 'Francia',
        requiereVisa: true
      }
    ];

    this.reservasNacionales = [
      {
        id: 3,
        personaId: 1,
        metodoTransporte: MetodoTransporte.TERRESTRE,
        fechaInicio: '2025-07-01',
        fechaFin: '2025-07-03',
        ciudadOrigen: 'Bogotá',
        ciudadDestino: 'Cartagena',
        precioTransporte: 95,
        hotel: 'Hotel Santa Clara',
        precioHospedaje: 440
      }
    ];
  }

  cargarInternacionales(): void {
    this.cargando = true;
    this.reservaIntService.getAll().subscribe({
      next:  data  => { this.reservasInternacionales = data; this.cargando = false; },
      error: err   => { this.error = 'Error al cargar reservas internacionales'; this.cargando = false; }
    });
  }

  cargarNacionales(): void {
    this.reservaNacService.getAll().subscribe({
      next:  data => { this.reservasNacionales = data; },
      error: err  => { this.error = 'Error al cargar reservas nacionales'; }
    });
  }

  descargarPdfInternacional(reserva: ReservaInternacionalModel): void {
    const nombre = localStorage.getItem('nombre') ?? 'Usuario TravelX';
    this.quotePdfService.generarInternacional(reserva, nombre);
  }

  descargarPdfNacional(reserva: ReservaNacionalModel): void {
    const nombre = localStorage.getItem('nombre') ?? 'Pasajero';
    this.quotePdfService.generarNacional(reserva, nombre);
  }

  get totalPrecioInternacional(): number {
    return this.reservasInternacionales.reduce(
      (acc, r) => acc + (r.precioTransporte ?? 0) + (r.precioHospedaje ?? 0), 0
    );
  }

  get totalPrecioNacional(): number {
    return this.reservasNacionales.reduce(
      (acc, r) => acc + (r.precioTransporte ?? 0) + (r.precioHospedaje ?? 0), 0
    );
  }

  volver(): void {
    this.router.navigate(['/inicio']);
  }
}
