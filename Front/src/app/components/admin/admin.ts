import { Component, OnInit  } from '@angular/core';
import { Router } from '@angular/router';
import {ReservaNacionalService} from '../../services/reserva-nacional.service';
import {ReservaInternacionalService} from '../../services/reserva-internacional.service';
import {PersonaService} from '../../services/persona.service';
import {PersonaModel, TipoUsuario} from '../../models/persona.model';
import {ReservaInternacionalModel} from '../../models/reserva-internacional.model';
import {ReservaNacionalModel} from '../../models/reserva-nacional.model';
import {MetodoTransporte} from '../../models/reserva.model';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.html',
  styleUrls: ['./admin.css']
})
export class AdminComponent implements OnInit{
  seccionActiva: 'usuarios' | 'administradores' | 'reservas' = 'usuarios';
  modalAbierto = false;
  modoEdicion = false;
  itemEditando: any = null;
  formulario: any = {};
  cargando = false;
  error = '';

  personas: PersonaModel[] = [];
  reservasInternacionales: ReservaInternacionalModel[] = [];
  reservasNacionales: ReservaNacionalModel[] = [];

  constructor(    private router: Router,
                  private personaService: PersonaService,
                  private reservaIntService: ReservaInternacionalService,
                  private reservaNacService: ReservaNacionalService) {}

  ngOnInit(): void {
    this.cargarTodo();
  }

  cargarTodo(): void {
    this.cargando = true;

    this.personaService.getAll().subscribe({
      next: data => { this.personas = data; this.cargando = false; },
      error: () => { this.error = 'Error al cargar personas'; this.cargando = false; }
    });

    this.reservaIntService.getAll().subscribe({
      next: data => { this.reservasInternacionales = data; },
      error: () => {}
    });

    this.reservaNacService.getAll().subscribe({
      next: data => { this.reservasNacionales = data; },
      error: () => {}
    });
  }

  get usuarios  (): any[] {
    return this.personas.filter(p => p.tipoUsuario === 'USUARIO').map(p => ({
      id: p.id,
      nombre: p.nombre,
      email: p.correo,
      documento: p.documento,
      estado: 'Activo'
    }));
  }

  get administradores(): any[] {
    return this.personas.filter(p => p.tipoUsuario === 'ADMINISTRADOR').map(p => ({
      id: p.id,
      nombre: p.nombre,
      email: p.correo,
      documento: p.documento,
      rol: 'Administrador'
    }));
  }
  get reservas(): any[] {
    const int = this.reservasInternacionales.map(r => ({
      id: r.id,
      origen: r.ciudadOrigen,
      destino: r.ciudadDestino,
      fechaInicio: r.fechaInicio,
      fechaFin: r.fechaFin,
      transporte: r.metodoTransporte,
      hotel: r.hotel,
      total: (r.precioTransporte + r.precioHospedaje),
      tipo: 'Internacional'
    }));

    const nac = this.reservasNacionales.map(r => ({
      id: r.id,
      origen: r.ciudadOrigen,
      destino: r.ciudadDestino,
      fechaInicio: r.fechaInicio,
      fechaFin: r.fechaFin,
      transporte: r.metodoTransporte,
      hotel: r.hotel,
      total: (r.precioTransporte + r.precioHospedaje),
      tipo: 'Nacional'
    }));

    return [...int, ...nac];
  }

  get datosActivos(): any[] {
    if (this.seccionActiva === 'usuarios') return this.usuarios;
    if (this.seccionActiva === 'administradores') return this.administradores;
    return this.reservas;
  }

  get columnasActivas(): string[] {
    if (this.seccionActiva === 'usuarios') return ['id', 'nombre', 'email', 'documento', 'estado'];
    if (this.seccionActiva === 'administradores') return ['id', 'nombre', 'email', 'rol'];
    return ['id', 'origen', 'destino', 'fechaInicio', 'transporte', 'hotel', 'total', 'tipo'];
  }

  abrirModalCrear() {
    this.formulario = {};
    this.modoEdicion = false;
    this.modalAbierto = true;
  }

  abrirModalEditar(item: any) {
    this.formulario = { ...item };
    this.itemEditando = item;
    this.modoEdicion = true;
    this.modalAbierto = true;
  }

  guardar() {
    const id = this.itemEditando?.id;

    if (this.seccionActiva === 'usuarios' || this.seccionActiva === 'administradores') {
      const persona: PersonaModel = {
        ...(this.modoEdicion && { id }),
        nombre:      this.formulario.nombre,
        documento:   this.formulario.documento,
        correo:      this.formulario.email,
        contrasena:  this.formulario.contrasena ?? '',
        tipoUsuario: this.seccionActiva === 'administradores' ? TipoUsuario.ADMINISTRADOR : TipoUsuario.USUARIO
      };

      const accion = this.modoEdicion
        ? this.personaService.update(id, persona)
        : this.personaService.create(persona);

      accion.subscribe({
        next: () => { this.modalAbierto = false; this.cargarTodo(); },
        error: () => { this.error = 'Error al guardar persona'; }
      });
      return;
    }

    if (this.seccionActiva === 'reservas') {
      const esInternacional = this.formulario.tipo === 'Internacional'
        || this.itemEditando?.tipo === 'Internacional';

      const personaId = Number(localStorage.getItem('personaId')) || 0;

      const baseReserva = {
        personaId:        personaId,
        ciudadOrigen:     this.formulario.origen,
        ciudadDestino:    this.formulario.destino,
        fechaInicio:      this.formulario.fechaInicio,
        fechaFin:         this.formulario.fechaFin,
        hotel:            this.formulario.hotel,
        precioHospedaje:  this.formulario.precioHospedaje,
        precioTransporte: this.formulario.precioTransporte,
        metodoTransporte: this.formulario.transporte as MetodoTransporte,
        ...(this.modoEdicion && { id })
      };

      if (esInternacional) {
        const reserva: ReservaInternacionalModel = {
          ...baseReserva,
          paisOrigen:   this.formulario.paisOrigen,
          paisDestino:  this.formulario.paisDestino,
          requiereVisa: this.formulario.requiereVisa ?? false
        };
        const accion = this.modoEdicion
          ? this.reservaIntService.update(id, reserva)
          : this.reservaIntService.create(reserva);

        accion.subscribe({
          next: () => { this.modalAbierto = false; this.cargarTodo(); },
          error: () => { this.error = 'Error al guardar reserva internacional'; }
        });
        return;
      }

      const reserva: ReservaNacionalModel = { ...baseReserva };
      const accion = this.modoEdicion
        ? this.reservaNacService.update(id, reserva)
        : this.reservaNacService.create(reserva);

      accion.subscribe({
        next: () => { this.modalAbierto = false; this.cargarTodo(); },
        error: () => { this.error = 'Error al guardar reserva nacional'; }
      });
    }
  }


  eliminar(item: any) {
    if (this.seccionActiva === 'usuarios' || this.seccionActiva === 'administradores') {
      this.personaService.delete(item.id).subscribe({ next: () => this.cargarTodo() });
      return;
    }

    if (this.seccionActiva === 'reservas') {
      const accion = item.tipo === 'Internacional'
        ? this.reservaIntService.delete(item.id)
        : this.reservaNacService.delete(item.id);

      accion.subscribe({ next: () => this.cargarTodo() });
    }
  }

  cerrarSesion() {
    this.router.navigate(['/login']);
  }
}
