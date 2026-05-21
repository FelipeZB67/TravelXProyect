import { Injectable } from '@angular/core';
import jsPDF from 'jspdf';
import { ReservaInternacionalModel } from '../models/reserva-internacional.model';
import { ReservaNacionalModel } from '../models/reserva-nacional.model';

@Injectable({ providedIn: 'root' })
export class QuotePdfService {

  generarInternacional(reserva: ReservaInternacionalModel, nombrePersona: string): void {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    this.buildPdf(doc, reserva, nombrePersona, {
      paisOrigen:   reserva.paisOrigen,
      paisDestino:  reserva.paisDestino,
      requiereVisa: reserva.requiereVisa
    });
    doc.save(`cotizacion-${reserva.ciudadDestino?.toLowerCase() ?? 'viaje'}.pdf`);
  }

  generarNacional(reserva: ReservaNacionalModel, nombrePersona: string): void {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    this.buildPdf(doc, reserva, nombrePersona, null);
    doc.save(`cotizacion-${reserva.ciudadDestino?.toLowerCase() ?? 'viaje'}.pdf`);
  }

  private buildPdf(
    doc: jsPDF,
    reserva: ReservaInternacionalModel | ReservaNacionalModel,
    nombrePersona: string,
    extra: { paisOrigen: string; paisDestino: string; requiereVisa: boolean } | null
  ): void {

    const orange  = [249, 115, 22]  as [number, number, number];
    const dark    = [15,  23,  42]  as [number, number, number];
    const gray    = [100, 116, 139] as [number, number, number];
    const light   = [241, 245, 249] as [number, number, number];
    const red     = [220, 38,  38]  as [number, number, number];
    const green   = [22,  163, 74]  as [number, number, number];

    // Header
    doc.setFillColor(...orange);
    doc.rect(0, 0, 210, 28, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(20);
    doc.setFont('helvetica', 'bold');
    doc.text('TravelX', 14, 12);
    doc.setFontSize(10);
    doc.setFont('helvetica', 'normal');
    doc.text('Cotización de Viaje', 14, 20);
    const fecha = new Date().toLocaleDateString('es-CO', { year: 'numeric', month: 'long', day: 'numeric' });
    doc.text(`Generado: ${fecha}`, 196, 20, { align: 'right' });

    let y = 36;

    // Pasajero
    doc.setTextColor(...dark);
    doc.setFontSize(13);
    doc.setFont('helvetica', 'bold');
    doc.text(nombrePersona, 14, y);
    y += 8;

    // Ruta
    const ruta = `${reserva.ciudadOrigen}  —  ${reserva.ciudadDestino}`;
    doc.setFontSize(15);
    doc.text(ruta, 14, y);
    y += 6;

    if (extra) {
      doc.setFontSize(10);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(...gray);
      doc.text(`${extra.paisOrigen}  —  ${extra.paisDestino}`, 14, y);
      y += 8;
    } else {
      y += 4;
    }

    // Métricas en cajas
    doc.setFillColor(...light);
    const cajas = [
      { label: 'TRANSPORTE',  valor: reserva.metodoTransporte ?? 'No especificada' },
      { label: 'FECHA INICIO', valor: reserva.fechaInicio ?? 'No especificada' },
      { label: 'FECHA FIN',    valor: reserva.fechaFin    ?? 'No especificada' },
      { label: 'HOTEL',        valor: reserva.hotel       ?? 'No especificado' },
    ];

    cajas.forEach((caja, i) => {
      const x = 14 + (i % 2) * 97;
      if (i % 2 === 0 && i > 0) y += 20;
      doc.setFillColor(241, 245, 249);
      doc.roundedRect(x, y, 90, 16, 3, 3, 'F');
      doc.setFontSize(8);
      doc.setFont('helvetica', 'normal');
      doc.setTextColor(100, 116, 139);
      doc.text(caja.label, x + 4, y + 6);
      doc.setFontSize(11);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(15, 23, 42);
      doc.text(String(caja.valor), x + 4, y + 13);
    });
    y += 28;

    // Desglose de precios
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(...dark);
    doc.text('Desglose de costos (USD)', 14, y);
    y += 3;
    doc.setLineWidth(0.3);
    doc.setDrawColor(...orange);
    doc.line(14, y, 196, y);
    y += 7;

    const lineas = [
      { label: 'Precio transporte', valor: reserva.precioTransporte ?? 0 },
      { label: 'Precio hospedaje',  valor: reserva.precioHospedaje  ?? 0 },
    ];
    const total = (reserva.precioTransporte ?? 0) + (reserva.precioHospedaje ?? 0);

    lineas.forEach(linea => {
      doc.setFont('helvetica', 'normal');
      doc.setFontSize(10);
      doc.setTextColor(...gray);
      doc.text(linea.label, 14, y);
      doc.setTextColor(...dark);
      doc.text(`$${linea.valor.toLocaleString('es-CO')}`, 196, y, { align: 'right' });
      y += 7;
    });

    doc.setLineWidth(0.4);
    doc.setDrawColor(...light);
    doc.line(14, y, 196, y);
    y += 6;
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(13);
    doc.setTextColor(...dark);
    doc.text('TOTAL ESTIMADO', 14, y);
    doc.setTextColor(...orange);
    doc.text(`$${total.toLocaleString('es-CO')} USD`, 196, y, { align: 'right' });
    y += 14;

    // Visa (solo internacional)
    if (extra !== null) {
      doc.setFillColor(...(extra.requiereVisa ? [254, 226, 226] : [220, 252, 231]) as [number,number,number]);
      doc.roundedRect(12, y, 184, 12, 3, 3, 'F');
      doc.setFontSize(9);
      doc.setFont('helvetica', 'bold');
      doc.setTextColor(...(extra.requiereVisa ? red : green));
      const visaTexto = extra.requiereVisa
        ? 'Requiere visa para este destino'
        : 'No requiere visa para este destino';
      doc.text(visaTexto, 16, y + 8);
      y += 18;
    }

    // Footer
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(...gray);
    doc.text('Esta cotización es informativa y está sujeta a disponibilidad y variación de tarifas.', 14, y);
    doc.text('TravelX · travelxoficial@gmail.com', 14, y + 5);
    doc.setTextColor(...orange);
    doc.text('Página 1 de 1', 196, y + 5, { align: 'right' });
  }
}
