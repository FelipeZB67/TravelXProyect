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
  modalEliminarAbierto = false;
  modoEdicion = false;

  itemEditando: any = null;
  itemEliminando: any = null;
  formulario: any = {};

  cargando = false;
  guardando = false;
  eliminando = false;

  error = '';
  errorModal = '';

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
      error: err => {
        this.error = this.obtenerMensajeError(err, 'No se pudieron cargar las personas.');
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });

    this.reservaService.todas().subscribe({
      next: data => {
        this.reservasGuardadas = data ?? [];
        this.cdr.detectChanges();
      },
      error: err => {
        this.reservasGuardadas = [];
        this.error = this.obtenerMensajeError(err, 'No se pudieron cargar las reservas.');
        this.cdr.detectChanges();
      }
    });
  }

  cambiarSeccion(seccion: 'usuarios' | 'administradores' | 'reservas'): void {
    this.seccionActiva = seccion;
    this.error = '';
    this.errorModal = '';
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
      username: r.username,
      origen: `${r.originCity ?? ''}, ${r.originCountry ?? ''}`.replace(/^, |, $/g, ''),
      destino: `${r.destinationCity ?? ''}, ${r.destinationCountry ?? ''}`.replace(/^, |, $/g, ''),
      fechaInicio: r.departureDate,
      fechaFin: r.returnDate,
      transporte: r.type,
      hotel: r.type === 'AIRBNB' || r.type === 'HOTEL' ? r.title : '',
      total: r.price ?? 0,
      tipo: this.tipoReserva(r.type),
      estado: r.available === false ? 'No disponible' : 'Guardada',
      reservaOriginal: r
    }));
  }

  get datosActivos(): any[] {
    if (this.seccionActiva === 'usuarios') return this.usuarios;
    if (this.seccionActiva === 'administradores') return this.administradores;
    return this.reservas;
  }

  get columnasActivas(): string[] {
    if (this.seccionActiva === 'usuarios') return ['id', 'nombre', 'email', 'documento', 'estado'];
    if (this.seccionActiva === 'administradores') return ['id', 'nombre', 'email', 'documento', 'rol'];
    return ['id', 'username', 'origen', 'destino', 'fechaInicio', 'fechaFin', 'tipo', 'total', 'estado'];
  }

  nombreColumna(columna: string): string {
    const nombres: Record<string, string> = {
      id: 'ID',
      username: 'Usuario',
      nombre: 'Nombre',
      email: 'Email',
      documento: 'Documento',
      estado: 'Estado',
      rol: 'Rol',
      origen: 'Origen',
      destino: 'Destino',
      fechaInicio: 'Ida',
      fechaFin: 'Regreso',
      transporte: 'Transporte',
      hotel: 'Hotel',
      total: 'Total',
      tipo: 'Tipo'
    };

    return nombres[columna] ?? columna;
  }

  abrirModalCrear(): void {
    this.errorModal = '';
    this.modoEdicion = false;
    this.itemEditando = null;

    if (this.seccionActiva === 'reservas') {
      this.formulario = this.formularioReservaVacio();
    } else {
      this.formulario = {
        nombre: '',
        email: '',
        documento: '',
        contrasena: ''
      };
    }

    this.modalAbierto = true;
    this.cdr.detectChanges();
  }

  abrirModalEditar(item: any): void {
    this.errorModal = '';
    this.modoEdicion = true;
    this.itemEditando = item;

    if (this.seccionActiva === 'reservas') {
      const reserva = item.reservaOriginal ?? item;
      this.formulario = {
        id: reserva.id,
        username: reserva.username ?? '',
        provider: reserva.provider ?? 'TRAVELX_ADMIN',
        type: reserva.type ?? 'FLIGHT',
        title: reserva.title ?? '',
        description: reserva.description ?? '',
        originCity: reserva.originCity ?? '',
        originCountry: reserva.originCountry ?? '',
        destinationCity: reserva.destinationCity ?? '',
        destinationCountry: reserva.destinationCountry ?? '',
        departureDate: reserva.departureDate ?? '',
        returnDate: reserva.returnDate ?? '',
        currency: reserva.currency ?? 'USD',
        price: reserva.price ?? 0,
        priceText: reserva.priceText ?? '',
        adults: reserva.adults ?? 1,
        children: reserva.children ?? 0,
        pets: reserva.pets ?? 0,
        travelClass: reserva.travelClass ?? 'ECONOMY',
        hasPool: reserva.hasPool ?? false,
        hasJacuzzi: reserva.hasJacuzzi ?? false,
        petFriendly: reserva.petFriendly ?? false,
        available: reserva.available ?? true,
        bookingUrl: reserva.bookingUrl ?? '',
        providerStatusCode: reserva.providerStatusCode ?? 200,
        providerSuccess: reserva.providerSuccess ?? true,
        providerMessage: reserva.providerMessage ?? 'Reserva gestionada por administrador.',
        providerResponse: reserva.providerResponse ?? ''
      };
    } else {
      this.formulario = {
        nombre: item.nombre ?? '',
        email: item.email ?? '',
        documento: item.documento ?? '',
        contrasena: ''
      };
    }

    this.modalAbierto = true;
    this.cdr.detectChanges();
  }

  guardar(): void {
    this.errorModal = '';
    this.error = '';

    if (this.seccionActiva === 'reservas') {
      this.guardarReserva();
      return;
    }

    this.guardarPersona();
  }

  guardarPersona(): void {
    const validacion = this.validarPersona();

    if (validacion) {
      this.errorModal = validacion;
      this.cdr.detectChanges();
      return;
    }

    const id = this.itemEditando?.id;

    const persona: PersonaModel = {
      ...(this.modoEdicion && { id }),
      nombre: this.formulario.nombre.trim(),
      documento: this.formulario.documento.trim(),
      correo: this.formulario.email.trim(),
      contrasena: this.modoEdicion ? 'NO_CAMBIAR_PASSWORD' : this.formulario.contrasena,
      tipoUsuario: this.seccionActiva === 'administradores' ? TipoUsuario.ADMINISTRADOR : TipoUsuario.USUARIO
    };

    this.guardando = true;
    this.cdr.detectChanges();

    const accion = this.modoEdicion
      ? this.personaService.update(id, persona)
      : this.personaService.create(persona);

    accion.subscribe({
      next: () => {
        this.guardando = false;
        this.modalAbierto = false;
        this.cargarTodo();
      },
      error: err => {
        this.guardando = false;
        this.errorModal = this.obtenerMensajeError(err, 'No se pudo guardar la persona.');
        this.cdr.detectChanges();
      }
    });
  }

  guardarReserva(): void {
    const validacion = this.validarReserva();

    if (validacion) {
      this.errorModal = validacion;
      return;
    }

    const reserva: TravelOptionModel = this.crearReservaDesdeFormulario();

    this.guardando = true;
    this.cdr.detectChanges();

    const accion = this.modoEdicion
      ? this.reservaService.actualizarAdmin(this.itemEditando.id, reserva)
      : this.reservaService.crearAdmin(reserva);

    accion.subscribe({
      next: () => {
        this.guardando = false;
        this.modalAbierto = false;
        this.cargarTodo();
      },
      error: err => {
        this.guardando = false;
        this.errorModal = this.obtenerMensajeError(err, 'No se pudo guardar la reserva.');
        this.cdr.detectChanges();
      }
    });
  }

  eliminar(item: any): void {
    this.itemEliminando = item;
    this.modalEliminarAbierto = true;
    this.error = '';
    this.errorModal = '';
    this.cdr.detectChanges();
  }

  cancelarEliminar(): void {
    this.modalEliminarAbierto = false;
    this.itemEliminando = null;
    this.eliminando = false;
    this.errorModal = '';
  }

  confirmarEliminar(): void {
    if (!this.itemEliminando?.id) return;

    this.eliminando = true;
    this.error = '';
    this.errorModal = '';
    this.cdr.detectChanges();

    if (this.seccionActiva === 'reservas') {
      this.reservaService.eliminarAdmin(this.itemEliminando.id).subscribe({
        next: () => {
          this.modalEliminarAbierto = false;
          this.itemEliminando = null;
          this.eliminando = false;
          this.cargarTodo();
        },
        error: err => {
          this.eliminando = false;
          this.errorModal = this.obtenerMensajeError(err, 'No se pudo eliminar la reserva.');
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
      error: err => {
        this.eliminando = false;
        this.errorModal = this.obtenerMensajeError(err, 'No se pudo eliminar la persona.');
        this.cdr.detectChanges();
      }
    });
  }

  tipoReserva(tipo?: string): string {
    if (tipo === 'FLIGHT') return 'Vuelo';
    if (tipo === 'BUS') return 'Terrestre';
    if (tipo === 'HOTEL') return 'Hotel';
    if (tipo === 'AIRBNB') return 'Airbnb';
    return tipo ?? 'Reserva';
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  private formularioReservaVacio(): any {
    return {
      username: '',
      provider: 'TRAVELX_ADMIN',
      type: 'FLIGHT',
      title: 'Reserva creada por administrador',
      description: 'Reserva registrada desde el panel administrativo.',
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
      hasPool: false,
      hasJacuzzi: false,
      petFriendly: false,
      available: true,
      bookingUrl: '',
      providerStatusCode: 200,
      providerSuccess: true,
      providerMessage: 'Reserva gestionada por administrador.',
      providerResponse: ''
    };
  }

  private crearReservaDesdeFormulario(): TravelOptionModel {
    const price = Number(this.formulario.price) || 0;
    const currency = this.formulario.currency?.trim() || 'USD';

    return {
      id: this.formulario.id,
      username: this.formulario.username?.trim(),
      provider: this.formulario.provider?.trim() || 'TRAVELX_ADMIN',
      type: this.formulario.type,
      title: this.formulario.title?.trim(),
      description: this.formulario.description?.trim(),
      originCity: this.formulario.originCity?.trim(),
      originCountry: this.formulario.originCountry?.trim(),
      destinationCity: this.formulario.destinationCity?.trim(),
      destinationCountry: this.formulario.destinationCountry?.trim(),
      departureDate: this.formulario.departureDate,
      returnDate: this.formulario.returnDate,
      currency,
      price,
      priceText: `${price} ${currency}`,
      adults: Number(this.formulario.adults) || 1,
      children: Number(this.formulario.children) || 0,
      pets: Number(this.formulario.pets) || 0,
      travelClass: this.formulario.travelClass || 'ECONOMY',
      hasPool: this.formulario.hasPool ?? false,
      hasJacuzzi: this.formulario.hasJacuzzi ?? false,
      petFriendly: this.formulario.petFriendly ?? false,
      available: this.formulario.available ?? true,
      bookingUrl: this.formulario.bookingUrl ?? '',
      providerStatusCode: this.formulario.providerStatusCode ?? 200,
      providerSuccess: this.formulario.providerSuccess ?? true,
      providerMessage: this.formulario.providerMessage ?? 'Reserva gestionada por administrador.',
      providerResponse: this.formulario.providerResponse ?? ''
    };
  }

  private validarPersona(): string {
    this.formulario.nombre = this.formulario.nombre?.trim() ?? '';
    this.formulario.email = this.formulario.email?.trim() ?? '';
    this.formulario.documento = this.formulario.documento?.trim() ?? '';

    if (!this.formulario.nombre) return 'Ingresa el nombre.';
    if (this.formulario.nombre.length < 3) return 'El nombre debe tener al menos 3 caracteres.';
    if (this.formulario.nombre.length > 60) return 'El nombre no puede tener más de 60 caracteres.';

    if (!this.formulario.email) return 'Ingresa el correo.';

    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!correoRegex.test(this.formulario.email)) {
      return 'Ingresa un correo válido.';
    }

    if (this.seccionActiva === 'administradores' && !this.formulario.email.toLowerCase().endsWith('@unbosque.edu.co')) {
      return 'Los administradores deben usar un correo @unbosque.edu.co.';
    }

    if (!this.formulario.documento) return 'Ingresa el documento.';

    if (!/^[0-9]+$/.test(this.formulario.documento)) {
      return 'El documento solo puede contener números.';
    }

    if (this.formulario.documento.length < 6) {
      return 'El documento debe tener al menos 6 dígitos.';
    }

    if (this.formulario.documento.length > 12) {
      return 'El documento no puede tener más de 12 dígitos.';
    }

    if (!this.modoEdicion && !this.formulario.contrasena?.trim()) {
      return 'Ingresa la contraseña.';
    }

    if (!this.modoEdicion && this.formulario.contrasena.length < 8) {
      return 'La contraseña debe tener al menos 8 caracteres.';
    }

    if (!this.modoEdicion && this.formulario.contrasena.length > 50) {
      return 'La contraseña no puede tener más de 50 caracteres.';
    }

    return '';
  }

  private validarReserva(): string {
    this.formulario.username = this.formulario.username?.trim() ?? '';
    this.formulario.provider = this.formulario.provider?.trim() ?? '';
    this.formulario.title = this.formulario.title?.trim() ?? '';
    this.formulario.originCity = this.formulario.originCity?.trim() ?? '';
    this.formulario.originCountry = this.formulario.originCountry?.trim() ?? '';
    this.formulario.destinationCity = this.formulario.destinationCity?.trim() ?? '';
    this.formulario.destinationCountry = this.formulario.destinationCountry?.trim() ?? '';
    this.formulario.currency = this.formulario.currency?.trim() ?? '';

    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!this.formulario.username) return 'Ingresa el correo del usuario.';
    if (!correoRegex.test(this.formulario.username)) return 'Ingresa un correo de usuario válido.';

    if (!this.formulario.provider) return 'Ingresa el proveedor.';
    if (!this.formulario.type) return 'Selecciona el tipo de reserva.';
    if (!this.formulario.title) return 'Ingresa el título.';
    if (this.formulario.title.length < 4) return 'El título debe tener al menos 4 caracteres.';

    if (this.formulario.type !== 'HOTEL' && this.formulario.type !== 'AIRBNB') {
      if (!this.formulario.originCity) return 'Ingresa la ciudad de origen.';
      if (!this.formulario.originCountry) return 'Ingresa el país de origen.';
    }

    if (!this.formulario.destinationCity) return 'Ingresa la ciudad de destino.';
    if (!this.formulario.destinationCountry) return 'Ingresa el país de destino.';
    if (!this.formulario.departureDate) return 'Selecciona la fecha de ida.';
    if (!this.formulario.returnDate) return 'Selecciona la fecha de regreso.';

    if (new Date(this.formulario.returnDate) < new Date(this.formulario.departureDate)) {
      return 'La fecha de regreso no puede ser anterior a la fecha de ida.';
    }

    if ((Number(this.formulario.adults) || 0) < 1) {
      return 'Debe haber al menos un adulto.';
    }

    if ((Number(this.formulario.children) || 0) < 0) {
      return 'La cantidad de niños no puede ser negativa.';
    }

    if ((Number(this.formulario.pets) || 0) < 0) {
      return 'La cantidad de mascotas no puede ser negativa.';
    }

    if (!this.formulario.currency) return 'Ingresa la moneda.';

    if ((Number(this.formulario.price) || 0) < 0) {
      return 'El precio no puede ser negativo.';
    }

    return '';
  }

  private obtenerMensajeError(err: any, mensajeDefault: string): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'Los datos enviados no son válidos.';
    if (err?.status === 401) return 'Tu sesión expiró. Inicia sesión nuevamente.';
    if (err?.status === 403) return 'No tienes permisos para realizar esta acción.';
    if (err?.status === 404) return mensajeBack || 'No se encontró el registro solicitado.';

    if (err?.status === 409) {
      if (mensajeBack?.toLowerCase().includes('correo')) return 'Ya existe una cuenta con ese correo.';
      if (mensajeBack?.toLowerCase().includes('documento')) return 'Ya existe una cuenta con ese documento.';
      return mensajeBack || 'Ya existe un registro con esos datos.';
    }

    if (err?.status >= 500) return 'El servidor tuvo un problema. Intenta de nuevo en unos minutos.';

    return mensajeBack || mensajeDefault;
  }
}
