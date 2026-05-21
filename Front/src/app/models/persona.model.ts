export interface PersonaModel{
  id?:          number;
  nombre:       string;
  documento:    string;
  correo:       string;
  contrasena:   string;
  tipoUsuario?: TipoUsuario;
}

export enum TipoUsuario {
  ADMINISTRADOR = 'ADMINISTRADOR',
  USUARIO       = 'USUARIO',
  NINGUNO       = 'NINGUNO'
}
