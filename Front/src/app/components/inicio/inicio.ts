import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TravelOptionModel } from '../../models/travel-option.model';
import { TravelSearchRequestModel } from '../../models/travel-search.model';
import { TravelSearchService } from '../../services/travel-search.service';
import { ReservaService } from '../../services/reserva.service';
import { LocationService } from '../../services/location.service';
import { AirportService } from '../../services/airport.service';
import { PersonaService } from '../../services/persona.service';
import { AuthService } from '../../services/auth.service';
import { PersonaModel, TipoUsuario } from '../../models/persona.model';

@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.html',
  standalone: false,
  styleUrls: ['./inicio.css']
})
export class InicioComponent implements OnInit {
  tabActivo: 'aereo' | 'terrestre' | 'hotel' = 'aereo';

  origenInput = 'Bogotá';
  paisOrigenInput = 'Colombia';
  destinoInput = 'Ciudad de México';
  paisDestinoInput = 'México';
  fechaSalidaInput = '2026-07-15';
  fechaRegresoInput = '2026-07-22';
  adultosInput = 1;
  ninosInput = 0;
  mascotasInput = 0;
  precioMinimoInput?: number;
  precioMaximoInput?: number;
  piscinaInput = false;
  jacuzziInput = false;
  petFriendlyInput = false;

  cargando = false;
  error = '';
  mensajeBusqueda = '';
  reservandoId = '';

  modalCuentaAbierto = false;
  modalEliminarCuentaAbierto = false;
  cargandoCuenta = false;
  guardandoCuenta = false;
  eliminandoCuenta = false;
  errorCuenta = '';
  mensajeCuenta = '';
  usuarioActual?: PersonaModel;
  cuentaFormulario = {
    nombre: '',
    correo: '',
    documento: '',
    contrasena: ''
  };

  paises: string[] = [
    'Afganistán',
    'Albania',
    'Alemania',
    'Andorra',
    'Angola',
    'Antigua y Barbuda',
    'Arabia Saudita',
    'Argelia',
    'Argentina',
    'Armenia',
    'Australia',
    'Austria',
    'Azerbaiyán',
    'Bahamas',
    'Bangladés',
    'Barbados',
    'Baréin',
    'Bélgica',
    'Belice',
    'Benín',
    'Bielorrusia',
    'Bolivia',
    'Bosnia y Herzegovina',
    'Botsuana',
    'Brasil',
    'Brunéi',
    'Bulgaria',
    'Burkina Faso',
    'Burundi',
    'Bután',
    'Cabo Verde',
    'Camboya',
    'Camerún',
    'Canadá',
    'Catar',
    'Chad',
    'Chile',
    'China',
    'Chipre',
    'Ciudad del Vaticano',
    'Colombia',
    'Comoras',
    'Corea del Norte',
    'Corea del Sur',
    'Costa de Marfil',
    'Costa Rica',
    'Croacia',
    'Cuba',
    'Dinamarca',
    'Dominica',
    'Ecuador',
    'Egipto',
    'El Salvador',
    'Emiratos Árabes Unidos',
    'Eritrea',
    'Eslovaquia',
    'Eslovenia',
    'España',
    'Estados Unidos',
    'Estonia',
    'Esuatini',
    'Etiopía',
    'Filipinas',
    'Finlandia',
    'Fiyi',
    'Francia',
    'Gabón',
    'Gambia',
    'Georgia',
    'Ghana',
    'Granada',
    'Grecia',
    'Guatemala',
    'Guinea',
    'Guinea-Bisáu',
    'Guinea Ecuatorial',
    'Guyana',
    'Haití',
    'Honduras',
    'Hungría',
    'India',
    'Indonesia',
    'Irak',
    'Irán',
    'Irlanda',
    'Islandia',
    'Islas Marshall',
    'Islas Salomón',
    'Israel',
    'Italia',
    'Jamaica',
    'Japón',
    'Jordania',
    'Kazajistán',
    'Kenia',
    'Kirguistán',
    'Kiribati',
    'Kuwait',
    'Laos',
    'Lesoto',
    'Letonia',
    'Líbano',
    'Liberia',
    'Libia',
    'Liechtenstein',
    'Lituania',
    'Luxemburgo',
    'Macedonia del Norte',
    'Madagascar',
    'Malasia',
    'Malaui',
    'Maldivas',
    'Malí',
    'Malta',
    'Marruecos',
    'Mauricio',
    'Mauritania',
    'México',
    'Micronesia',
    'Moldavia',
    'Mónaco',
    'Mongolia',
    'Montenegro',
    'Mozambique',
    'Myanmar',
    'Namibia',
    'Nauru',
    'Nepal',
    'Nicaragua',
    'Níger',
    'Nigeria',
    'Noruega',
    'Nueva Zelanda',
    'Omán',
    'Países Bajos',
    'Pakistán',
    'Palaos',
    'Palestina',
    'Panamá',
    'Papúa Nueva Guinea',
    'Paraguay',
    'Perú',
    'Polonia',
    'Portugal',
    'Reino Unido',
    'República Centroafricana',
    'República Checa',
    'República del Congo',
    'República Democrática del Congo',
    'República Dominicana',
    'Ruanda',
    'Rumania',
    'Rusia',
    'Samoa',
    'San Cristóbal y Nieves',
    'San Marino',
    'San Vicente y las Granadinas',
    'Santa Lucía',
    'Santo Tomé y Príncipe',
    'Senegal',
    'Serbia',
    'Seychelles',
    'Sierra Leona',
    'Singapur',
    'Siria',
    'Somalia',
    'Sri Lanka',
    'Sudáfrica',
    'Sudán',
    'Sudán del Sur',
    'Suecia',
    'Suiza',
    'Surinam',
    'Tailandia',
    'Tanzania',
    'Tayikistán',
    'Timor Oriental',
    'Togo',
    'Tonga',
    'Trinidad y Tobago',
    'Túnez',
    'Turkmenistán',
    'Turquía',
    'Tuvalu',
    'Ucrania',
    'Uganda',
    'Uruguay',
    'Uzbekistán',
    'Vanuatu',
    'Venezuela',
    'Vietnam',
    'Yemen',
    'Yibuti',
    'Zambia',
    'Zimbabue'
  ];

  opcionesBusqueda: TravelOptionModel[] = [];

  constructor(
    private router: Router,
    private travelSearchService: TravelSearchService,
    private reservaService: ReservaService,
    private locationService: LocationService,
    private airportService: AirportService,
    private personaService: PersonaService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {}

  cambiarTab(tab: 'aereo' | 'terrestre' | 'hotel'): void {
    this.tabActivo = tab;
    this.error = '';
    this.mensajeBusqueda = '';
    this.opcionesBusqueda = [];
    this.cargando = false;
    this.reservandoId = '';
  }

  buscar(): void {
    this.error = '';
    this.mensajeBusqueda = '';

    const validacion = this.validarFormularioBusqueda();

    if (validacion) {
      this.error = validacion;
      return;
    }

    this.cargando = true;
    this.opcionesBusqueda = [];
    this.cdr.detectChanges();

    this.validarUbicacionesAntesDeBuscar();
  }

  validarUbicacionesAntesDeBuscar(): void {
    this.locationService.validarCiudadPais(this.destinoInput.trim(), this.paisDestinoInput).subscribe({
      next: destino => {
        if (!destino?.found) {
          this.detenerCarga(`No se encontró la ciudad de destino en ${this.paisDestinoInput}.`);
          return;
        }

        if (!this.paisCoincide(destino.country, this.paisDestinoInput)) {
          this.detenerCarga(`La ciudad de destino no coincide con ${this.paisDestinoInput}.`);
          return;
        }

        if (this.tabActivo === 'hotel') {
          this.ejecutarBusqueda();
          return;
        }

        this.locationService.validarCiudadPais(this.origenInput.trim(), this.paisOrigenInput).subscribe({
          next: origen => {
            if (!origen?.found) {
              this.detenerCarga(`No se encontró la ciudad de origen en ${this.paisOrigenInput}.`);
              return;
            }

            if (!this.paisCoincide(origen.country, this.paisOrigenInput)) {
              this.detenerCarga(`La ciudad de origen no coincide con ${this.paisOrigenInput}.`);
              return;
            }

            if (this.tabActivo === 'aereo') {
              this.validarAeropuertosAntesDeBuscar(origen.countryCode, destino.countryCode);
              return;
            }

            this.ejecutarBusqueda();
          },
          error: err => {
            this.detenerCarga(this.obtenerMensajeError(err, `No se pudo validar la ciudad de origen en ${this.paisOrigenInput}.`));
          }
        });
      },
      error: err => {
        this.detenerCarga(this.obtenerMensajeError(err, `No se pudo validar la ciudad de destino en ${this.paisDestinoInput}.`));
      }
    });
  }

  validarAeropuertosAntesDeBuscar(codigoPaisOrigen?: string, codigoPaisDestino?: string): void {
    if (!codigoPaisOrigen || !codigoPaisDestino) {
      this.detenerCarga('No se pudo validar el país de origen o destino para buscar aeropuertos.');
      return;
    }

    this.airportService.buscarAeropuerto(this.origenInput.trim(), codigoPaisOrigen).subscribe({
      next: aeropuertoOrigen => {
        if (!aeropuertoOrigen?.found) {
          this.detenerCarga(aeropuertoOrigen?.message || `No se encontró aeropuerto para ${this.origenInput} en ${this.paisOrigenInput}.`);
          return;
        }

        this.airportService.buscarAeropuerto(this.destinoInput.trim(), codigoPaisDestino).subscribe({
          next: aeropuertoDestino => {
            if (!aeropuertoDestino?.found) {
              this.detenerCarga(aeropuertoDestino?.message || `No se encontró aeropuerto para ${this.destinoInput} en ${this.paisDestinoInput}.`);
              return;
            }

            this.ejecutarBusqueda();
          },
          error: err => {
            this.detenerCarga(this.obtenerMensajeError(err, `No se pudo validar aeropuerto para ${this.destinoInput} en ${this.paisDestinoInput}.`));
          }
        });
      },
      error: err => {
        this.detenerCarga(this.obtenerMensajeError(err, `No se pudo validar aeropuerto para ${this.origenInput} en ${this.paisOrigenInput}.`));
      }
    });
  }

  ejecutarBusqueda(): void {
    const request = this.crearRequest();

    const consulta = this.tabActivo === 'hotel'
      ? this.travelSearchService.buscarHospedaje(request)
      : this.travelSearchService.buscarTodo(request);

    consulta.subscribe({
      next: response => {
        this.opcionesBusqueda = response.options ?? [];
        this.mensajeBusqueda = response.message || this.mensajeBusquedaExitosa();
        this.cargando = false;

        if (this.itemsActivos.length === 0) {
          this.mensajeBusqueda = '';
          this.error = 'No se encontraron opciones disponibles para esta búsqueda.';
        }

        this.cdr.detectChanges();
      },
      error: err => {
        this.detenerCarga(this.obtenerMensajeError(err, 'No se pudo completar la búsqueda. Revisa los datos e intenta de nuevo.'));
      }
    });
  }

  reservar(option: TravelOptionModel): void {
    if (!option.available) {
      this.error = option.providerMessage || 'Esta opción no está disponible para reservar.';
      return;
    }

    this.reservandoId = this.claveReserva(option);
    this.error = '';
    this.cdr.detectChanges();

    this.reservaService.agregar(option).subscribe({
      next: () => {
        this.reservandoId = '';
        this.router.navigate(['/cotizacion']);
      },
      error: err => {
        this.error = this.obtenerMensajeError(err, 'No se pudo guardar la reserva.');
        this.reservandoId = '';
        this.cdr.detectChanges();
      }
    });
  }

  abrirCuenta(): void {
    const correo = this.authService.getCorreo();

    this.errorCuenta = '';
    this.mensajeCuenta = '';
    this.cuentaFormulario.contrasena = '';
    this.modalCuentaAbierto = true;
    this.cargandoCuenta = true;
    this.cdr.detectChanges();

    if (!correo) {
      this.errorCuenta = 'No se encontró la información de sesión.';
      this.cargandoCuenta = false;
      this.cdr.detectChanges();
      return;
    }

    this.personaService.getByCorreo(correo).subscribe({
      next: persona => {
        this.usuarioActual = persona;
        this.cuentaFormulario = {
          nombre: persona.nombre ?? '',
          correo: persona.correo ?? '',
          documento: persona.documento ?? '',
          contrasena: ''
        };
        this.cargandoCuenta = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.errorCuenta = this.obtenerMensajeError(err, 'No se pudieron cargar los datos de tu cuenta.');
        this.cargandoCuenta = false;
        this.cdr.detectChanges();
      }
    });
  }

  cerrarCuenta(): void {
    if (this.guardandoCuenta || this.eliminandoCuenta) return;

    this.modalCuentaAbierto = false;
    this.modalEliminarCuentaAbierto = false;
    this.errorCuenta = '';
    this.mensajeCuenta = '';
    this.cuentaFormulario.contrasena = '';
  }

  guardarCuenta(): void {
    this.errorCuenta = '';
    this.mensajeCuenta = '';

    const validacion = this.validarCuenta();

    if (validacion) {
      this.errorCuenta = validacion;
      return;
    }

    if (!this.usuarioActual?.id) {
      this.errorCuenta = 'No se encontró el identificador de tu cuenta.';
      return;
    }

    const persona: PersonaModel = {
      id: this.usuarioActual.id,
      nombre: this.cuentaFormulario.nombre.trim(),
      documento: this.cuentaFormulario.documento.trim(),
      correo: this.cuentaFormulario.correo.trim(),
      contrasena: this.cuentaFormulario.contrasena.trim() || 'NO_CAMBIAR_PASSWORD',
      tipoUsuario: this.usuarioActual.tipoUsuario ?? TipoUsuario.USUARIO
    };

    this.guardandoCuenta = true;
    this.cdr.detectChanges();

    this.personaService.update(this.usuarioActual.id, persona).subscribe({
      next: () => {
        this.guardandoCuenta = false;
        this.usuarioActual = persona;
        this.authService.actualizarDatosSesion(persona.nombre);
        this.mensajeCuenta = 'Cuenta actualizada correctamente.';
        this.cuentaFormulario.contrasena = '';
        this.cdr.detectChanges();
      },
      error: err => {
        this.guardandoCuenta = false;
        this.errorCuenta = this.obtenerMensajeError(err, 'No se pudo actualizar tu cuenta.');
        this.cdr.detectChanges();
      }
    });
  }

  abrirEliminarCuenta(): void {
    this.errorCuenta = '';
    this.mensajeCuenta = '';
    this.modalEliminarCuentaAbierto = true;
  }

  cancelarEliminarCuenta(): void {
    if (this.eliminandoCuenta) return;
    this.modalEliminarCuentaAbierto = false;
  }

  confirmarEliminarCuenta(): void {
    if (!this.usuarioActual?.id) {
      this.errorCuenta = 'No se encontró el identificador de tu cuenta.';
      return;
    }

    this.eliminandoCuenta = true;
    this.errorCuenta = '';
    this.mensajeCuenta = '';
    this.cdr.detectChanges();

    this.personaService.delete(this.usuarioActual.id).subscribe({
      next: () => {
        localStorage.clear();
        this.router.navigate(['/login']);
      },
      error: err => {
        this.eliminandoCuenta = false;
        this.errorCuenta = this.obtenerMensajeError(err, 'No se pudo eliminar tu cuenta.');
        this.cdr.detectChanges();
      }
    });
  }

  get itemsActivos(): TravelOptionModel[] {
    return this.opcionesBusqueda.filter(option => {
      if (this.tabActivo === 'aereo') return option.type === 'FLIGHT';
      if (this.tabActivo === 'terrestre') return option.type === 'BUS';
      return option.type === 'AIRBNB' || option.type === 'HOTEL';
    });
  }

  get heroBackground(): string {
    if (this.tabActivo === 'aereo') return 'url(/imagenes/hero-flight.jpg)';
    if (this.tabActivo === 'terrestre') return 'url(/imagenes/hero-bus.webp)';
    return 'url(/imagenes/hero-hotel.jpg)';
  }

  imagenItem(option: TravelOptionModel): string {
    if (option.type === 'FLIGHT') return '/imagenes/card-flight.jpg';
    if (option.type === 'BUS') return '/imagenes/card-bus.jpg';
    return '/imagenes/card-hotel.jpg';
  }

  precioItem(option: TravelOptionModel): string {
    if (option.priceText) return option.priceText;
    if (option.price !== undefined && option.price !== null) return `${option.price} ${option.currency ?? 'USD'}`;
    return 'Precio no disponible';
  }

  descripcionItem(option: TravelOptionModel): string {
    if (option.type === 'FLIGHT' && option.available) {
      return 'Resultado relacionado encontrado por Google Flights.';
    }

    if (option.type === 'BUS' && option.available) {
      return option.description || 'Ruta terrestre disponible para tu viaje.';
    }

    if ((option.type === 'AIRBNB' || option.type === 'HOTEL') && option.available) {
      return option.description || 'Hospedaje relacionado encontrado para tu destino.';
    }

    if (option.providerMessage) return option.providerMessage;

    return 'Proveedor no disponible en este momento.';
  }

  claveReserva(option: TravelOptionModel): string {
    return `${option.provider ?? ''}-${option.type ?? ''}-${option.title ?? ''}`;
  }

  paisCoincide(paisApi: string | undefined, paisSeleccionado: string): boolean {
    return this.normalizarTexto(paisApi ?? '') === this.normalizarTexto(paisSeleccionado);
  }

  normalizarTexto(valor: string): string {
    return valor
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toLowerCase();
  }

  nombreCiudadValido(valor: string): boolean {
    const normalizado = this.normalizarTexto(valor);
    const partes = normalizado.split(/\s+/).filter(Boolean);
    const ultimaPalabra = partes[partes.length - 1];

    if (normalizado.length < 4) {
      return false;
    }

    return !['de', 'del', 'la', 'las', 'el', 'los', 'san', 'santa'].includes(ultimaPalabra);
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  irACotizacion(): void {
    this.router.navigate(['/cotizacion']);
  }

  private validarCuenta(): string {
    this.cuentaFormulario.nombre = this.cuentaFormulario.nombre.trim();
    this.cuentaFormulario.documento = this.cuentaFormulario.documento.trim();

    if (!this.cuentaFormulario.nombre) {
      return 'Ingresa tu nombre.';
    }

    if (this.cuentaFormulario.nombre.length < 3) {
      return 'El nombre debe tener al menos 3 caracteres.';
    }

    if (this.cuentaFormulario.nombre.length > 60) {
      return 'El nombre no puede tener más de 60 caracteres.';
    }

    if (!this.cuentaFormulario.documento) {
      return 'Ingresa tu documento.';
    }

    if (!/^[0-9]+$/.test(this.cuentaFormulario.documento)) {
      return 'El documento solo puede contener números.';
    }

    if (this.cuentaFormulario.documento.length < 6) {
      return 'El documento debe tener al menos 6 dígitos.';
    }

    if (this.cuentaFormulario.documento.length > 12) {
      return 'El documento no puede tener más de 12 dígitos.';
    }

    if (this.cuentaFormulario.contrasena && this.cuentaFormulario.contrasena.length < 8) {
      return 'La nueva contraseña debe tener al menos 8 caracteres.';
    }

    if (this.cuentaFormulario.contrasena && this.cuentaFormulario.contrasena.length > 50) {
      return 'La nueva contraseña no puede tener más de 50 caracteres.';
    }

    return '';
  }

  private validarFormularioBusqueda(): string {
    if (!this.nombreCiudadValido(this.destinoInput)) {
      return 'Completa el nombre de la ciudad de destino.';
    }

    if (!this.paisDestinoInput) {
      return 'Selecciona el país de destino.';
    }

    if (this.tabActivo !== 'hotel' && !this.nombreCiudadValido(this.origenInput)) {
      return 'Completa el nombre de la ciudad de origen.';
    }

    if (this.tabActivo !== 'hotel' && !this.paisOrigenInput) {
      return 'Selecciona el país de origen.';
    }

    if (!this.fechaSalidaInput || !this.fechaRegresoInput) {
      return 'Selecciona la fecha de ida y la fecha de regreso.';
    }

    if (new Date(this.fechaRegresoInput) < new Date(this.fechaSalidaInput)) {
      return 'La fecha de regreso no puede ser anterior a la fecha de ida.';
    }

    if ((Number(this.adultosInput) || 0) < 1) {
      return 'Debe viajar al menos un adulto.';
    }

    if ((Number(this.ninosInput) || 0) < 0) {
      return 'La cantidad de niños no puede ser negativa.';
    }

    if ((Number(this.mascotasInput) || 0) < 0) {
      return 'La cantidad de mascotas no puede ser negativa.';
    }

    if (this.precioMinimoInput !== undefined && this.precioMinimoInput < 0) {
      return 'El precio mínimo no puede ser negativo.';
    }

    if (this.precioMaximoInput !== undefined && this.precioMaximoInput < 0) {
      return 'El precio máximo no puede ser negativo.';
    }

    if (
      this.precioMinimoInput !== undefined &&
      this.precioMaximoInput !== undefined &&
      this.precioMaximoInput < this.precioMinimoInput
    ) {
      return 'El precio máximo no puede ser menor que el precio mínimo.';
    }

    return '';
  }

  private mensajeBusquedaExitosa(): string {
    if (this.tabActivo === 'aereo') return 'Búsqueda de vuelos finalizada exitosamente.';
    if (this.tabActivo === 'terrestre') return 'Búsqueda de rutas terrestres finalizada exitosamente.';
    return 'Búsqueda de hospedajes finalizada exitosamente.';
  }

  private detenerCarga(mensaje: string): void {
    this.error = mensaje;
    this.mensajeBusqueda = '';
    this.cargando = false;
    this.cdr.detectChanges();
  }

  private obtenerMensajeError(err: any, mensajeDefault: string): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'Los datos enviados no son válidos.';
    if (err?.status === 401) return 'Tu sesión expiró. Inicia sesión nuevamente.';
    if (err?.status === 403) return 'No tienes permisos para realizar esta acción.';
    if (err?.status === 404) return mensajeBack || 'No se encontró la información solicitada.';
    if (err?.status === 409) return mensajeBack || 'La solicitud entra en conflicto con un registro existente.';
    if (err?.status === 429) return 'Se hicieron demasiadas solicitudes. Espera un momento e intenta de nuevo.';
    if (err?.status >= 500) return 'El servidor tuvo un problema. Intenta de nuevo en unos minutos.';

    return mensajeBack || mensajeDefault;
  }

  private crearRequest(): TravelSearchRequestModel {
    return {
      ciudadOrigen: this.tabActivo === 'hotel' ? this.destinoInput.trim() : this.origenInput.trim(),
      paisOrigen: this.tabActivo === 'hotel' ? this.paisDestinoInput : this.paisOrigenInput,
      ciudadDestino: this.destinoInput.trim(),
      paisDestino: this.paisDestinoInput,
      fechaSalida: this.fechaSalidaInput,
      fechaRegreso: this.fechaRegresoInput,
      incluirTransporteTerrestre: this.tabActivo === 'terrestre',
      adultos: Number(this.adultosInput) || 1,
      ninos: Number(this.ninosInput) || 0,
      mascotas: Number(this.mascotasInput) || 0,
      moneda: 'USD',
      claseViaje: 'ECONOMY',
      incluirVuelos: this.tabActivo === 'aereo',
      incluirAirbnb: this.tabActivo === 'hotel',
      incluirHoteles: this.tabActivo === 'hotel',
      precioMinimo: this.precioMinimoInput,
      precioMaximo: this.precioMaximoInput,
      piscina: this.piscinaInput,
      jacuzzi: this.jacuzziInput,
      petFriendly: this.petFriendlyInput
    };
  }
}
