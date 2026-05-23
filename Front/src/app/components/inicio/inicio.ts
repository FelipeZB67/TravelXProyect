import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TravelOptionModel } from '../../models/travel-option.model';
import { TravelSearchRequestModel } from '../../models/travel-search.model';
import { TravelSearchService } from '../../services/travel-search.service';
import { ReservaService } from '../../services/reserva.service';
import { LocationService } from '../../services/location.service';
import { AirportService } from '../../services/airport.service';

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
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {}

  cambiarTab(tab: 'aereo' | 'terrestre' | 'hotel'): void {
    this.tabActivo = tab;
    this.error = '';
    this.mensajeBusqueda = '';
    this.opcionesBusqueda = [];
  }

  buscar(): void {
    this.error = '';
    this.mensajeBusqueda = '';

    if (!this.nombreCiudadValido(this.destinoInput)) {
      this.error = 'Completa el nombre de la ciudad de destino.';
      return;
    }

    if (this.tabActivo !== 'hotel' && !this.nombreCiudadValido(this.origenInput)) {
      this.error = 'Completa el nombre de la ciudad de origen.';
      return;
    }

    if (!this.destinoInput.trim()) {
      this.error = 'Ingresa la ciudad de destino.';
      return;
    }

    if (!this.paisDestinoInput) {
      this.error = 'Selecciona el país de destino.';
      return;
    }

    if (this.tabActivo !== 'hotel' && !this.origenInput.trim()) {
      this.error = 'Ingresa la ciudad de origen.';
      return;
    }

    if (this.tabActivo !== 'hotel' && !this.paisOrigenInput) {
      this.error = 'Selecciona el país de origen.';
      return;
    }

    if (!this.fechaSalidaInput || !this.fechaRegresoInput) {
      this.error = 'Selecciona la fecha de ida y la fecha de regreso.';
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
        if (!this.paisCoincide(destino.country, this.paisDestinoInput)) {
          this.error = `La ciudad de destino no coincide con ${this.paisDestinoInput}.`;
          this.cargando = false;
          this.cdr.detectChanges();
          return;
        }

        if (this.tabActivo === 'hotel') {
          this.ejecutarBusqueda();
          return;
        }

        this.locationService.validarCiudadPais(this.origenInput.trim(), this.paisOrigenInput).subscribe({
          next: origen => {
            if (!this.paisCoincide(origen.country, this.paisOrigenInput)) {
              this.error = `La ciudad de origen no coincide con ${this.paisOrigenInput}.`;
              this.cargando = false;
              this.cdr.detectChanges();
              return;
            }

            if (this.tabActivo === 'aereo') {
              this.validarAeropuertosAntesDeBuscar(origen.countryCode, destino.countryCode);
              return;
            }

            this.ejecutarBusqueda();
          },
          error: () => {
            this.error = `No se encontró la ciudad de origen en ${this.paisOrigenInput}.`;
            this.cargando = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: () => {
        this.error = `No se encontró la ciudad de destino en ${this.paisDestinoInput}.`;
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  validarAeropuertosAntesDeBuscar(codigoPaisOrigen?: string, codigoPaisDestino?: string): void {
    if (!codigoPaisOrigen || !codigoPaisDestino) {
      this.error = 'No se pudo validar el país de origen o destino para buscar aeropuertos.';
      this.cargando = false;
      this.cdr.detectChanges();
      return;
    }

    this.airportService.buscarAeropuerto(this.origenInput.trim(), codigoPaisOrigen).subscribe({
      next: aeropuertoOrigen => {
        if (!aeropuertoOrigen.found) {
          this.error = `No se encontró aeropuerto para ${this.origenInput} en ${this.paisOrigenInput}.`;
          this.cargando = false;
          this.cdr.detectChanges();
          return;
        }

        this.airportService.buscarAeropuerto(this.destinoInput.trim(), codigoPaisDestino).subscribe({
          next: aeropuertoDestino => {
            if (!aeropuertoDestino.found) {
              this.error = `No se encontró aeropuerto para ${this.destinoInput} en ${this.paisDestinoInput}.`;
              this.cargando = false;
              this.cdr.detectChanges();
              return;
            }

            this.ejecutarBusqueda();
          },
          error: () => {
            this.error = `No se encontró aeropuerto para ${this.destinoInput} en ${this.paisDestinoInput}.`;
            this.cargando = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: () => {
        this.error = `No se encontró aeropuerto para ${this.origenInput} en ${this.paisOrigenInput}.`;
        this.cargando = false;
        this.cdr.detectChanges();
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
        this.mensajeBusqueda = response.message ?? '';
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.error?.message || err.error || 'No se pudo completar la búsqueda. Revisa que la ciudad corresponda al país seleccionado.';
        this.cargando = false;
        this.cdr.detectChanges();
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
        this.error = err.error?.message || err.error || `No se pudo guardar la reserva. Estado: ${err.status}`;
        this.reservandoId = '';
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
    if (this.tabActivo === 'aereo') return 'url(https://images.unsplash.com/photo-1436491865332-7a61a109cc05?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YXZpb24lMjBkZSUyMHZpYWplfGVufDB8fDB8fHww)';
    if (this.tabActivo === 'terrestre') return 'url(https://alohacamp.com/es/travels/wp-content/uploads/2024/09/montanas-de-espana-ZW.webp)';
    return 'url(https://imagenes2.eltiempo.com/files/image_1200_535/uploads/2024/04/22/6626a322cf16f.jpeg)';
  }

  imagenItem(option: TravelOptionModel): string {
    if (option.type === 'FLIGHT') return 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=600&auto=format&fit=crop';
    if (option.type === 'BUS') return 'https://images.unsplash.com/photo-1559521783-1d1599583485?w=600&auto=format&fit=crop';
    return 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop';
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
