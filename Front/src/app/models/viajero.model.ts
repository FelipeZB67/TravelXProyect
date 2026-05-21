export interface ViajeroModel {
  id?:              number;
  nombre:           string;
  pasaporte:        string;
  fechaNacimiento:  string;   // LocalDate → string 'YYYY-MM-DD'
  esMayor:          boolean;
}
