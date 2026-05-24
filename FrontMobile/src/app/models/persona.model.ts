/** Representa los datos de una persona registrada en el sistema. */
export interface PersonaModel{
  id?:          number;
  nombre:       string;
  documento:    string;
  correo:       string;
  contrasena:   string;
  tipoUsuario?: TipoUsuario;
}

/** Define los roles disponibles para los usuarios del sistema. */
export enum TipoUsuario {
  ADMINISTRADOR = 'ADMINISTRADOR',
  USUARIO       = 'USUARIO',
  NINGUNO       = 'NINGUNO'
}
