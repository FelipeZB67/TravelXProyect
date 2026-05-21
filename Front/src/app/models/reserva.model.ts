export interface Reserva {
  id?:               number;
  personaId:         number;
  metodoTransporte:  MetodoTransporte;
  fechaInicio:       string;   // LocalDate → string 'YYYY-MM-DD'
  fechaFin:          string;
  ciudadOrigen:      string;
  ciudadDestino:     string;
  precioTransporte:  number;
  hotel:             string;
  precioHospedaje:   number;
}

export enum MetodoTransporte {
  TERRESTRE = 'TERRESTRE',
  AEREO     = 'AEREO'
}
