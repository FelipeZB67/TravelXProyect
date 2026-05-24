import { TravelOptionModel } from './travel-option.model';

/** Parámetros de búsqueda enviados al servicio de viajes. */
export interface TravelSearchRequestModel {
  ciudadOrigen: string;
  paisOrigen: string;
  ciudadDestino: string;
  paisDestino: string;
  fechaSalida: string;
  fechaRegreso: string;
  incluirTransporteTerrestre: boolean;
  adultos: number;
  ninos: number;
  mascotas: number;
  moneda: string;
  claseViaje: string;
  incluirVuelos: boolean;
  incluirAirbnb: boolean;
  incluirHoteles: boolean;
  precioMinimo?: number;
  precioMaximo?: number;
  piscina?: boolean;
  jacuzzi?: boolean;
  petFriendly?: boolean;
}

/** Representa una ubicación geográfica resuelta a partir de una búsqueda por ciudad y país. */
export interface ResolvedLocationModel {
  query?: string;
  displayName?: string;
  latitude?: string;
  longitude?: string;
  country?: string;
  countryCode?: string;
  found?: boolean;
}

/** Respuesta del servidor ante una búsqueda de opciones de viaje. */
export interface TravelSearchResponseModel {
  success?: boolean;
  message?: string;
  request?: TravelSearchRequestModel;
  resolvedOrigin?: ResolvedLocationModel;
  resolvedDestination?: ResolvedLocationModel;
  options?: TravelOptionModel[];
}
