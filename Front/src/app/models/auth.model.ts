export interface LoginRequest {
  correo:     string;
  contrasena: string;
}

export interface AuthResponse {
  token:       string;
  tipoUsuario: string;
  nombre:      string;
  correo:      string;
}

export interface VerifyEmailRequest {
  correo:  string;
  codigo:  string;
}
