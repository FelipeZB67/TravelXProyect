package co.edu.unbosque.travelx.controller;

import co.edu.unbosque.travelx.dto.PersonaDTO;
import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.security.JwtUtil;
import co.edu.unbosque.travelx.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación de personas en TravelX.
 * Gestiona el inicio de sesión, el registro de nuevas cuentas y la
 * verificación de correo electrónico mediante código.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "API para autenticación de personas en TravelX")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final PersonaService personaService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PersonaService personaService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.personaService = personaService;
	}

	 /**
     * Autentica una persona con su correo y contraseña. Si las credenciales son
     * válidas, genera y retorna un token JWT junto con la información básica del usuario.
     *
     * @param loginRequest objeto con el correo y contraseña de la persona
     * @return {@link ResponseEntity} con el token JWT y datos del usuario en código 200,
     *         o 401 si las credenciales son inválidas
     */
	@Operation(summary = "Iniciar sesión", description = """
			    Autentica una persona usando correo y contraseña.
			    Si las credenciales son válidas, retorna un token JWT y el tipo de usuario.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
					    {
					      "token": "eyJhbGciOiJIUzI1NiJ9...",
					      "tipoUsuario": "ADMINISTRADOR"
					    }
					"""))),
			@ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Correo o contraseña inválidos"))) })
	@PostMapping("/login")
	public ResponseEntity<?> login(
			@Parameter(description = "Credenciales de la persona", required = true, schema = @Schema(implementation = PersonaDTO.class), examples = @ExampleObject(value = """
										    {
					  "correo": "admin@unbosque.edu.co",
					  "contrasena": "1234567890"
					}
										""")) @RequestBody PersonaDTO loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getContrasena()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(userDetails);

			String tipoUsuario = null;
			String nombre = null;
			String correo = null;

			if (userDetails instanceof Persona) {
				Persona persona = (Persona) userDetails;
				tipoUsuario = persona.getTipoUsuario().name();
				nombre = persona.getNombre();
				correo = persona.getCorreo();
			}

			return ResponseEntity.ok(new AuthResponse(jwt, tipoUsuario, nombre, correo));

		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña inválidos");
		}
	}

	 /**
     * Registra una nueva persona en TravelX. El endpoint es público y no requiere
     * autenticación. Retorna códigos de conflicto si el nombre, correo o documento
     * ya se encuentran registrados.
     *
     * @param registerRequest objeto con los datos de la nueva persona a registrar
     * @return {@link ResponseEntity} con código 201 si el registro es exitoso,
     *         409 si hay datos duplicados, 400 si ocurre un error de validación,
     *         o 502 si falla el envío del correo de verificación
     */
	@Operation(summary = "Registrar nueva persona", description = """
			    Crea una nueva cuenta en TravelX.

			    Este endpoint es público, no requiere autenticación.
			    Por seguridad, el rol inicial debe manejarse desde el servicio.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Persona registrada exitosamente", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Persona registrada exitosamente"))),
			@ApiResponse(responseCode = "409", description = "Datos duplicados", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "El correo ya está registrado"))),
			@ApiResponse(responseCode = "400", description = "Error al registrar", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "Error al registrar la persona"))) })
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody PersonaDTO registerRequest) {
		try {
			int result = personaService.registerWithVerification(registerRequest);

			switch (result) {
			case 0:
				return ResponseEntity.status(HttpStatus.CREATED)
						.body("Persona registrada. Revisa tu correo para verificar la cuenta.");
			case 1:
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya existe");
			case 2:
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
			case 3:
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El documento ya está registrado");
			case 5:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Los administradores deben registrarse con un correo @unbosque.edu.co");
			default:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar la persona");
			}

		} catch (org.springframework.mail.MailException e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("No se pudo enviar el correo de verificación. Revisa la configuración SMTP.");
		}
	}

	 /**
     * Verifica el correo electrónico de una persona usando el código enviado
     * al momento del registro.
     *
     * @param request objeto con el correo y el código de verificación
     * @return {@link ResponseEntity} con código 200 si la verificación es exitosa o el correo
     *         ya estaba verificado, 404 si no existe la cuenta, o 400 si el código es inválido
     */
	@PostMapping("/verify")
	public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest request) {

		int result = personaService.verifyEmail(request.getCorreo(), request.getCodigo());

		switch (result) {
		case 0:
			return ResponseEntity.ok("Correo verificado exitosamente");
		case 1:
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe una cuenta con ese correo");
		case 2:
			return ResponseEntity.ok("El correo ya estaba verificado");
		case 3:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de verificación inválido");
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al verificar correo");
		}
	}

	/**
     * Clase interna que representa la respuesta devuelta tras un login exitoso,
     * conteniendo el token JWT y la información básica del usuario autenticado.
     */
	private static class AuthResponse {

		private final String token;
		private final String tipoUsuario;
		private final String nombre;
		private final String correo;

		public AuthResponse(String token, String tipoUsuario, String nombre, String correo) {
			this.token = token;
			this.tipoUsuario = tipoUsuario;
			this.nombre = nombre;
			this.correo = correo;
		}

		public String getToken() {
			return token;
		}

		public String getTipoUsuario() {
			return tipoUsuario;
		}

		public String getNombre() {
			return nombre;
		}

		public String getCorreo() {
			return correo;
		}
	}

	 /**
     * Clase estática que representa el cuerpo de la petición para verificar
     * el correo electrónico de una persona.
     */
	public static class VerifyEmailRequest {
		private String correo;
		private String codigo;

		public String getCorreo() {
			return correo;
		}

		public void setCorreo(String correo) {
			this.correo = correo;
		}

		public String getCodigo() {
			return codigo;
		}

		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
	}
}
