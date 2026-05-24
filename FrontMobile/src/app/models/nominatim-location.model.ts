/** Representa un resultado de ubicación retornado por la API de Nominatim. */
export interface NominatimLocationModel {
  place_id:     number;
  display_name: string;
  lat:          string;
  lon:          string;
  type:         string;
  class:        string;
}
