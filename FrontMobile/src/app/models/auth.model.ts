/** Datos requeridos para iniciar sesión. */
export interface LoginRequest {
  correo:     string;
  contrasena: string;
}

/** Respuesta del servidor tras una autenticación exitosa. */
export interface AuthResponse {
  token:       string;
  tipoUsuario: string;
  nombre:      string;
  correo:      string;
}

/** Datos requeridos para verificar el correo electrónico de un usuario. */
export interface VerifyEmailRequest {
  correo:  string;
  codigo:  string;
}
