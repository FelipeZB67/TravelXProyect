import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PersonaService } from '../../services/persona.service';
import { ReservaService } from '../../services/reserva.service';
import { PersonaModel, TipoUsuario } from '../../models/persona.model';
import { TravelOptionModel } from '../../models/travel-option.model';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.html',
  styleUrls: ['./admin.css']
})
export class AdminComponent implements OnInit {
  seccionActiva: 'usuarios' | 'administradores' | 'reservas' = 'usuarios';
  modalAbierto = false;
  modoEdicion = false;
  itemEditando: any = null;
  formulario: any = {};
  cargando = false;
  error = '';
  modalEliminarAbierto = false;
  itemEliminando: any = null;
  eliminando = false;

  personas: PersonaModel[] = [];
  reservasGuardadas: TravelOptionModel[] = [];

  constructor(
    private router: Router,
    private personaService: PersonaService,
    private reservaService: ReservaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarTodo();
  }

  cargarTodo(): void {
    this.cargando = true;
    this.error = '';
    this.cdr.detectChanges();

    this.personaService.getAll().subscribe({
      next: data => {
        this.personas = data ?? [];
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al cargar personas';
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });

    this.reservaService.todas().subscribe({
      next: data => {
        this.reservasGuardadas = data ?? [];
        this.cdr.detectChanges();
      },
      error: () => {
        this.reservasGuardadas = [];
        this.cdr.detectChanges();
      }
    });
  }

  get usuarios(): any[] {
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
    return this.reservasGuardadas.map(r => ({
      id: r.id,
      usuario: r.username,
      origen: `${r.originCity ?? ''}, ${r.originCountry ?? ''}`,
      destino: `${r.destinationCity ?? ''}, ${r.destinationCountry ?? ''}`,
      ida: r.departureDate,
      regreso: r.returnDate,
      tipo: this.tipoReservaAdmin(r.type),
      total: r.price ?? 0,
      estado: r.available ? 'Disponible' : 'No disponible'
    }));
  }

  get datosActivos(): any[] {
    if (this.seccionActiva === 'usuarios') return this.usuarios;
    if (this.seccionActiva === 'administradores') return this.administradores;
    return this.reservas;
  }

  get columnasActivas(): string[] {
    if (this.seccionActiva === 'usuarios') return ['id', 'nombre', 'email', 'documento', 'estado'];
    if (this.seccionActiva === 'administradores') return ['id', 'nombre', 'email', 'rol'];
    return ['id', 'usuario', 'origen', 'destino', 'ida', 'regreso', 'tipo', 'total', 'estado'];
  }

  tipoReservaAdmin(tipo?: string): string {
    if (tipo === 'FLIGHT') return 'Vuelo';
    if (tipo === 'BUS') return 'Terrestre';
    if (tipo === 'AIRBNB') return 'Airbnb';
    if (tipo === 'HOTEL') return 'Hotel';
    return tipo ?? 'Reserva';
  }

  nombreColumna(columna: string): string {
    const nombres: Record<string, string> = {
      id: 'ID',
      nombre: 'Nombre',
      email: 'Email',
      documento: 'Documento',
      estado: 'Estado',
      rol: 'Rol',
      usuario: 'Usuario',
      origen: 'Origen',
      destino: 'Destino',
      ida: 'Ida',
      regreso: 'Regreso',
      tipo: 'Tipo',
      total: 'Total'
    };

    return nombres[columna] ?? columna;
  }

  abrirModalCrear(): void {
    if (this.seccionActiva === 'reservas') {
      this.formulario = {
        username: '',
        provider: 'TRAVELX_ADMIN',
        type: 'FLIGHT',
        title: '',
        description: '',
        originCity: '',
        originCountry: '',
        destinationCity: '',
        destinationCountry: '',
        departureDate: '',
        returnDate: '',
        currency: 'USD',
        price: 0,
        priceText: '',
        adults: 1,
        children: 0,
        pets: 0,
        travelClass: 'ECONOMY',
        available: true,
        providerSuccess: true,
        providerStatusCode: 200
      };
    } else {
      this.formulario = {
        nombre: '',
        email: '',
        documento: '',
        contrasena: ''
      };
    }

    this.modoEdicion = false;
    this.itemEditando = null;
    this.modalAbierto = true;
    this.cdr.detectChanges();
  }

  abrirModalEditar(item: any): void {
    if (this.seccionActiva === 'reservas') {
      const reservaOriginal = this.reservasGuardadas.find(reserva => reserva.id === item.id);

      this.formulario = {
        id: reservaOriginal?.id,
        username: reservaOriginal?.username ?? '',
        provider: reservaOriginal?.provider ?? 'TRAVELX_ADMIN',
        type: reservaOriginal?.type ?? 'FLIGHT',
        title: reservaOriginal?.title ?? '',
        description: reservaOriginal?.description ?? '',
        originCity: reservaOriginal?.originCity ?? '',
        originCountry: reservaOriginal?.originCountry ?? '',
        destinationCity: reservaOriginal?.destinationCity ?? '',
        destinationCountry: reservaOriginal?.destinationCountry ?? '',
        departureDate: reservaOriginal?.departureDate ?? '',
        returnDate: reservaOriginal?.returnDate ?? '',
        currency: reservaOriginal?.currency ?? 'USD',
        price: reservaOriginal?.price ?? 0,
        priceText: reservaOriginal?.priceText ?? '',
        adults: reservaOriginal?.adults ?? 1,
        children: reservaOriginal?.children ?? 0,
        pets: reservaOriginal?.pets ?? 0,
        travelClass: reservaOriginal?.travelClass ?? 'ECONOMY',
        available: reservaOriginal?.available ?? true,
        providerSuccess: reservaOriginal?.providerSuccess ?? true,
        providerStatusCode: reservaOriginal?.providerStatusCode ?? 200
      };
    } else {
      this.formulario = {
        nombre: item.nombre ?? '',
        email: item.email ?? '',
        documento: item.documento ?? '',
        contrasena: ''
      };
    }

    this.itemEditando = item;
    this.modoEdicion = true;
    this.modalAbierto = true;
    this.cdr.detectChanges();
  }

  guardar(): void {
    const id = this.itemEditando?.id;

    if (this.seccionActiva === 'reservas') {
      const reserva: TravelOptionModel = {
        username: this.formulario.username,
        provider: this.formulario.provider,
        type: this.formulario.type,
        title: this.formulario.title,
        description: this.formulario.description,
        originCity: this.formulario.originCity,
        originCountry: this.formulario.originCountry,
        destinationCity: this.formulario.destinationCity,
        destinationCountry: this.formulario.destinationCountry,
        departureDate: this.formulario.departureDate,
        returnDate: this.formulario.returnDate,
        currency: this.formulario.currency,
        price: Number(this.formulario.price) || 0,
        priceText: this.formulario.priceText || `${Number(this.formulario.price) || 0} ${this.formulario.currency || 'USD'}`,
        adults: Number(this.formulario.adults) || 1,
        children: Number(this.formulario.children) || 0,
        pets: Number(this.formulario.pets) || 0,
        travelClass: this.formulario.travelClass,
        available: this.formulario.available === true || this.formulario.available === 'true',
        providerSuccess: this.formulario.providerSuccess === true || this.formulario.providerSuccess === 'true',
        providerStatusCode: Number(this.formulario.providerStatusCode) || 200
      };

      const accion = this.modoEdicion && id
        ? this.reservaService.actualizarAdmin(id, reserva)
        : this.reservaService.crearAdmin(reserva);

      accion.subscribe({
        next: () => {
          this.modalAbierto = false;
          this.cargarTodo();
        },
        error: err => {
          this.error = err.error?.message || err.error || 'Error al guardar reserva';
          this.cdr.detectChanges();
        }
      });
      return;
    }

    if (this.seccionActiva === 'usuarios' || this.seccionActiva === 'administradores') {
      const persona: PersonaModel = {
        ...(this.modoEdicion && { id }),
        nombre: this.formulario.nombre,
        documento: this.formulario.documento,
        correo: this.formulario.email,
        contrasena: this.modoEdicion ? 'NO_CAMBIAR_PASSWORD' : this.formulario.contrasena ?? '',
        tipoUsuario: this.seccionActiva === 'administradores' ? TipoUsuario.ADMINISTRADOR : TipoUsuario.USUARIO
      };

      const accion = this.modoEdicion
        ? this.personaService.update(id, persona)
        : this.personaService.create(persona);

      accion.subscribe({
        next: () => {
          this.modalAbierto = false;
          this.cargarTodo();
        },
        error: err => {
          this.error = err.error || 'Error al guardar persona';
          this.cdr.detectChanges();
        }
      });
    }
  }

  eliminar(item: any): void {
    this.itemEliminando = item;
    this.modalEliminarAbierto = true;
    this.error = '';
    this.cdr.detectChanges();
  }

  cancelarEliminar(): void {
    this.modalEliminarAbierto = false;
    this.itemEliminando = null;
    this.eliminando = false;
  }

  confirmarEliminar(): void {
    if (!this.itemEliminando?.id) return;

    this.eliminando = true;
    this.error = '';
    this.cdr.detectChanges();

    if (this.seccionActiva === 'reservas') {
      this.reservaService.eliminarAdmin(this.itemEliminando.id).subscribe({
        next: () => {
          this.modalEliminarAbierto = false;
          this.itemEliminando = null;
          this.eliminando = false;
          this.cargarTodo();
        },
        error: () => {
          this.error = 'Error al eliminar reserva';
          this.eliminando = false;
          this.cdr.detectChanges();
        }
      });

      return;
    }

    this.personaService.delete(this.itemEliminando.id).subscribe({
      next: () => {
        this.modalEliminarAbierto = false;
        this.itemEliminando = null;
        this.eliminando = false;
        this.cargarTodo();
      },
      error: () => {
        this.error = 'Error al eliminar persona';
        this.eliminando = false;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
