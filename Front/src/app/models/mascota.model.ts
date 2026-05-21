export interface MascotaModel {
  id?:              number;
  especie:          string;
  raza:             string;
  fechaNacimiento:  string;   // LocalDate → string 'YYYY-MM-DD'
}
