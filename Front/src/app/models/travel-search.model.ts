import { TravelOptionModel } from './travel-option.model';

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

export interface ResolvedLocationModel {
  query?: string;
  displayName?: string;
  latitude?: string;
  longitude?: string;
  country?: string;
  countryCode?: string;
  found?: boolean;
}

export interface TravelSearchResponseModel {
  success?: boolean;
  message?: string;
  request?: TravelSearchRequestModel;
  resolvedOrigin?: ResolvedLocationModel;
  resolvedDestination?: ResolvedLocationModel;
  options?: TravelOptionModel[];
}
