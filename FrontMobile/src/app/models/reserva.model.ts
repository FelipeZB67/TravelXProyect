/** Representa los datos de una reserva de viaje asociada a una persona. */
export interface Reserva {
  id?:               number;
  personaId:         number;
  metodoTransporte:  MetodoTransporte;
  fechaInicio:       string;
  fechaFin:          string;
  ciudadOrigen:      string;
  ciudadDestino:     string;
  precioTransporte:  number;
  hotel:             string;
  precioHospedaje:   number;
}

/** Define los métodos de transporte disponibles para una reserva. */
export enum MetodoTransporte {
  TERRESTRE = 'TERRESTRE',
  AEREO     = 'AEREO'
}
