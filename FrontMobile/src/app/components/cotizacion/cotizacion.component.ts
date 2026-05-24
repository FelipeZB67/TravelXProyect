import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TravelOptionModel } from '../../models/travel-option.model';
import { ReservaService } from '../../services/reserva.service';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-cotizacion',
  standalone: false,
  templateUrl: './cotizacion.component.html',
  styleUrl: './cotizacion.component.css'
})

/**
 * Componente para la visualización y gestión de cotizaciones del usuario.
 * Permite consultar reservas guardadas, filtrarlas por tipo y generar
 * comprobantes en formato PDF con el detalle de cada reserva.
 */
export class CotizacionComponent implements OnInit {
  tabActivo: 'transporte' | 'hospedaje' = 'transporte';

  reservas: TravelOptionModel[] = [];
  cargando = false;
  error = '';
  descargandoId = '';
  modalEliminarAbierto = false;
  reservaEliminando?: TravelOptionModel;
  eliminando = false;

  constructor(
    private router: Router,
    private reservaService: ReservaService,
    private cdr: ChangeDetectorRef
  ) {}

  /** Carga las reservas del usuario al inicializar el componente. */
  ngOnInit(): void {
    this.cargarReservas();
  }

  /**
   * Obtiene las reservas del usuario desde el servicio de reservas.
   * Actualiza el estado de carga y maneja los errores de la petición.
   */
  cargarReservas(): void {
    this.cargando = true;
    this.error = '';
    this.cdr.detectChanges();

    this.reservaService.misReservas().subscribe({
      next: data => {
        this.reservas = data ?? [];
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = this.obtenerMensajeError(err, 'No se pudieron cargar tus cotizaciones.');
        this.cargando = false;
        this.cdr.detectChanges();
      }
    });
  }

  /**
   * Cambia el tab activo entre transporte y hospedaje.
   *
   * @param tab tab a activar
   */
  cambiarTab(tab: 'transporte' | 'hospedaje'): void {
    this.tabActivo = tab;
    this.error = '';
  }

  /** Retorna las reservas de tipo vuelo o bus. */
  get reservasTransporte(): TravelOptionModel[] {
    return this.reservas.filter(reserva => reserva.type === 'FLIGHT' || reserva.type === 'BUS');
  }

  /** Retorna las reservas de tipo Airbnb u hotel. */
  get reservasHospedaje(): TravelOptionModel[] {
    return this.reservas.filter(reserva => reserva.type === 'AIRBNB' || reserva.type === 'HOTEL');
  }

  /** Retorna las reservas del tab activo. */
  get reservasActivas(): TravelOptionModel[] {
    return this.tabActivo === 'transporte' ? this.reservasTransporte : this.reservasHospedaje;
  }

  /** Calcula el precio total de todas las reservas de transporte. */
  get totalTransporte(): number {
    return this.reservasTransporte.reduce((acc, reserva) => acc + (reserva.price ?? 0), 0);
  }

  /** Calcula el precio total de todas las reservas de hospedaje. */
  get totalHospedaje(): number {
    return this.reservasHospedaje.reduce((acc, reserva) => acc + (reserva.price ?? 0), 0);
  }

  /** Retorna el total correspondiente al tab activo. */
  get totalActivo(): number {
    return this.tabActivo === 'transporte' ? this.totalTransporte : this.totalHospedaje;
  }

  /**
   * Convierte el tipo interno de una reserva a su nombre legible en español.
   *
   * @param reserva reserva cuyo tipo se desea traducir
   * @returns nombre legible del tipo de reserva
   */
  tipoReserva(reserva: TravelOptionModel): string {
    if (reserva.type === 'FLIGHT') return 'Aéreo';
    if (reserva.type === 'BUS') return 'Terrestre';
    if (reserva.type === 'AIRBNB') return 'Airbnb';
    if (reserva.type === 'HOTEL') return 'Hotel';
    return reserva.type ?? 'Reserva';
  }

  /**
   * Retorna la clase de ícono FontAwesome correspondiente al tipo de reserva.
   *
   * @param reserva reserva de la que se obtiene el ícono
   * @returns clase CSS del ícono
   */
  iconoReserva(reserva: TravelOptionModel): string {
    if (reserva.type === 'FLIGHT') return 'fas fa-plane';
    if (reserva.type === 'BUS') return 'fas fa-road';
    return 'fas fa-hotel';
  }

  /**
   * Retorna la clase CSS asociada al tipo de reserva para aplicar estilos visuales.
   *
   * @param reserva reserva de la que se obtiene la clase
   * @returns nombre de la clase CSS
   */
  claseReserva(reserva: TravelOptionModel): string {
    if (reserva.type === 'FLIGHT') return 'internacional';
    if (reserva.type === 'BUS') return 'nacional';
    return 'hospedaje';
  }

  /**
   * Genera el texto formateado del precio de una reserva.
   *
   * @param reserva reserva de la que se obtiene el precio
   * @returns texto con el precio y la moneda, o un mensaje si no está disponible
   */
  precioReserva(reserva: TravelOptionModel): string {
    if (reserva.priceText) return reserva.priceText;
    if (reserva.price !== undefined && reserva.price !== null) return `${reserva.price.toLocaleString('es-CO')} ${reserva.currency ?? 'USD'}`;
    return 'Precio no disponible';
  }

  /**
   * Indica si una reserva está siendo descargada en este momento.
   *
   * @param reserva reserva a verificar
   * @returns true si la reserva está en proceso de descarga
   */
  esDescargaActiva(reserva: TravelOptionModel): boolean {
    return this.descargandoId === `${reserva.id ?? ''}`;
  }

  /**
   * Genera y descarga el comprobante PDF de una reserva.
   * Construye el documento con encabezado visual, itinerario de viaje y detalle de compra.
   *
   * @param reserva reserva de la que se genera el comprobante
   */
  async imprimir(reserva: TravelOptionModel): Promise<void> {
    if (!reserva.id) {
      this.error = 'No se encontró el identificador de la cotización.';
      return;
    }

    this.descargandoId = `${reserva.id}`;
    this.error = '';
    this.cdr.detectChanges();

    try {
      const doc = new jsPDF('p', 'mm', 'a4');
      const nombreArchivo = `comprobante-travelx-${reserva.id}.pdf`;

      const imagenVuelo = 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=1600&auto=format&fit=crop';
      const imagenHeader = await this.cargarImagenPdf(imagenVuelo);

      if (imagenHeader) {
        doc.addImage(imagenHeader, 'JPEG', 0, 0, 210, 62);
      } else {
        doc.setFillColor(10, 34, 64);
        doc.rect(0, 0, 210, 62, 'F');
      }

      doc.setTextColor(255, 255, 255);
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(24);
      doc.text('TravelX', 16, 22);

      doc.setFont('helvetica', 'normal');
      doc.setFontSize(11);
      doc.text('Comprobante de reserva', 16, 31);

      doc.setFont('helvetica', 'bold');
      doc.setFontSize(17);
      doc.text(this.tipoReserva(reserva), 158, 22);

      doc.setFont('helvetica', 'normal');
      doc.setFontSize(9);
      doc.text(`Reserva TX-${reserva.id}`, 158, 31);

      doc.setFillColor(255, 255, 255);
      doc.roundedRect(15, 48, 180, 26, 4, 4, 'F');

      doc.setTextColor(12, 35, 58);
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(8);
      doc.text('ESTADO', 23, 59);
      doc.text('FECHA DE EMISIÓN', 78, 59);
      doc.text('PROVEEDOR', 137, 59);

      doc.setFont('helvetica', 'normal');
      doc.setFontSize(10);
      doc.text(reserva.available === false ? 'No disponible' : 'Disponible', 23, 67);
      doc.text(new Date().toLocaleDateString('es-CO'), 78, 67);
      doc.text(this.textoPdf(reserva.provider), 137, 67);

      doc.setTextColor(15, 29, 48);
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(18);
      doc.text('Itinerario de viaje', 16, 92);

      doc.setDrawColor(35, 145, 235);
      doc.setLineWidth(0.9);
      doc.line(16, 98, 194, 98);

      doc.setFillColor(239, 247, 255);
      doc.roundedRect(16, 107, 178, 42, 4, 4, 'F');

      doc.setFontSize(8);
      doc.setTextColor(84, 105, 130);
      doc.setFont('helvetica', 'bold');
      doc.text('ORIGEN', 26, 119);
      doc.text('DESTINO', 143, 119);

      doc.setTextColor(12, 35, 58);
      doc.setFontSize(15);
      doc.text(this.textoPdf(reserva.originCity), 26, 130);
      doc.text(this.textoPdf(reserva.destinationCity), 143, 130);

      doc.setFontSize(9);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(82, 101, 126);
      doc.text(this.textoPdf(reserva.originCountry), 26, 138);
      doc.text(this.textoPdf(reserva.destinationCountry), 143, 138);

      doc.setTextColor(35, 145, 235);
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(18);
      doc.text('>', 103, 131);

      doc.setTextColor(15, 29, 48);
      doc.setFontSize(9);
      doc.setFont('helvetica', 'bold');
      doc.text('Fecha de ida', 26, 162);
      doc.text('Fecha de regreso', 72, 162);
      doc.text('Clase', 122, 162);
      doc.text('Pasajeros', 156, 162);

      doc.setFont('helvetica', 'normal');
      doc.setTextColor(82, 101, 126);
      doc.text(this.textoPdf(reserva.departureDate), 26, 170);
      doc.text(this.textoPdf(reserva.returnDate), 72, 170);
      doc.text(this.textoPdf(reserva.travelClass), 122, 170);
      doc.text(`${reserva.adults ?? 0} adulto(s), ${reserva.children ?? 0} niño(s)`, 156, 170);

      doc.setTextColor(15, 29, 48);
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(16);
      doc.text('Detalle de compra', 16, 192);

      doc.setFillColor(255, 255, 255);
      doc.roundedRect(16, 201, 178, 44, 3, 3, 'F');
      doc.setDrawColor(220, 230, 242);
      doc.setLineWidth(0.7);
      doc.roundedRect(16, 201, 178, 44, 3, 3, 'S');

      doc.setFontSize(8);
      doc.setTextColor(84, 105, 130);
      doc.setFont('helvetica', 'bold');
      doc.text('SERVICIO', 24, 214);
      doc.text('DESCRIPCIÓN', 83, 214);
      doc.text('TOTAL', 171, 214);

      const servicio = doc.splitTextToSize(reserva.title ?? this.tipoReserva(reserva), 48);
      const detalle = doc.splitTextToSize(reserva.description || reserva.providerMessage || 'Reserva generada en TravelX.', 68);

      doc.setTextColor(12, 35, 58);
      doc.setFontSize(9);
      doc.setFont('helvetica', 'bold');
      doc.text(servicio, 24, 225);

      doc.setFont('helvetica', 'normal');
      doc.text(detalle, 83, 225);

      doc.setFont('helvetica', 'bold');
      doc.setTextColor(35, 145, 235);
      doc.text(this.precioReserva(reserva), 171, 225);

      doc.setFillColor(15, 29, 48);
      doc.roundedRect(16, 258, 178, 18, 3, 3, 'F');

      doc.setTextColor(255, 255, 255);
      doc.setFontSize(10);
      doc.setFont('helvetica', 'normal');
      doc.text('Total pagado', 24, 270);

      doc.setFont('helvetica', 'bold');
      doc.setFontSize(15);
      doc.text(this.precioReserva(reserva), 176, 270, { align: 'right' });

      doc.setTextColor(98, 116, 138);
      doc.setFontSize(8);
      doc.setFont('helvetica', 'normal');
      doc.text('Comprobante generado por TravelX', 16, 287);
      doc.text('Gracias por viajar con TravelX.', 194, 287, { align: 'right' });

      doc.save(nombreArchivo);
    } catch {
      this.error = 'No se pudo generar el comprobante. Intenta de nuevo.';
    } finally {
      this.descargandoId = '';
      this.cdr.detectChanges();
    }
  }

  abrirEliminar(reserva: TravelOptionModel): void {
    this.error = '';

    if (!reserva.id) {
      this.error = 'No se encontró el identificador de la reserva.';
      return;
    }

    this.reservaEliminando = reserva;
    this.modalEliminarAbierto = true;
  }

  cancelarEliminar(): void {
    if (this.eliminando) return;

    this.modalEliminarAbierto = false;
    this.reservaEliminando = undefined;
  }

  confirmarEliminar(): void {
    if (!this.reservaEliminando?.id) {
      this.error = 'No se encontró el identificador de la reserva.';
      return;
    }

    this.eliminando = true;
    this.error = '';

    this.reservaService.eliminar(this.reservaEliminando.id).subscribe({
      next: () => {
        const idEliminado = this.reservaEliminando?.id;
        this.reservas = this.reservas.filter(reserva => reserva.id !== idEliminado);
        this.eliminando = false;
        this.modalEliminarAbierto = false;
        this.reservaEliminando = undefined;
      },
      error: err => {
        this.error = this.obtenerMensajeError(err, 'No se pudo eliminar la reserva.');
        this.eliminando = false;
      }
    });
  }

  /** Navega hacia la vista de inicio. */
  volver(): void {
    this.router.navigate(['/inicio']);
  }

  private cargarImagenPdf(url: string): Promise<string | null> {
    return new Promise(resolve => {
      const imagen = new Image();
      imagen.crossOrigin = 'anonymous';

      imagen.onload = () => {
        const canvas = document.createElement('canvas');
        canvas.width = 1600;
        canvas.height = 520;

        const ctx = canvas.getContext('2d');

        if (!ctx) {
          resolve(null);
          return;
        }

        ctx.filter = 'blur(5px)';
        ctx.drawImage(imagen, -35, -35, 1670, 590);
        ctx.filter = 'none';
        ctx.fillStyle = 'rgba(8, 28, 54, 0.68)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        resolve(canvas.toDataURL('image/jpeg', 0.92));
      };

      imagen.onerror = () => resolve(null);
      imagen.src = url;
    });
  }

  private textoPdf(valor?: string): string {
    return valor && valor.trim() ? valor.trim() : 'No disponible';
  }

  private obtenerMensajeError(err: any, mensajeDefault: string): string {
    const mensajeBack = typeof err?.error === 'string'
      ? err.error
      : err?.error?.message;

    if (err?.status === 0) return 'No se pudo conectar con el servidor. Revisa que el back esté corriendo.';
    if (err?.status === 400) return mensajeBack || 'Los datos enviados no son válidos.';
    if (err?.status === 401) return 'Tu sesión expiró. Inicia sesión nuevamente.';
    if (err?.status === 403) return 'No tienes permisos para ver estas cotizaciones.';
    if (err?.status === 404) return mensajeBack || 'No se encontraron cotizaciones.';
    if (err?.status >= 500) return 'El servidor tuvo un problema al cargar tus cotizaciones. Intenta de nuevo en unos minutos.';

    return mensajeBack || mensajeDefault;
  }
}
