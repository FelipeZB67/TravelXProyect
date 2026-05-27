package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.PersonaDTO;
import co.edu.unbosque.travelx.entity.Persona.TipoUsuario;
import co.edu.unbosque.travelx.service.PersonaService;
import co.edu.unbosque.travelx.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Controlador REST para la administración de personas en TravelX.
 * Expone endpoints para crear, consultar, actualizar y eliminar personas,
 * delegando la lógica al servicio {@link PersonaService}.
 */
@RestController
@RequestMapping("/persona")
@Tag(name = "Gestión de Personas", description = "Endpoints para administrar personas")
@SecurityRequirement(name = "bearerAuth")
public class PersonaController {

	@Autowired
	private PersonaService personaService;
	@Autowired
	private JwtUtil jwtUtil;

	public PersonaController() {
	}

	/**
	 * Crea una nueva persona recibiendo los datos en formato JSON.
	 *
	 * @param nueva objeto con los datos de la persona a crear
	 * @return {@link ResponseEntity} con código 201 si es exitoso, 409 si hay datos
	 *         duplicados, o 400 si ocurre un error de validación
	 */
	@Operation(summary = "Crear persona (JSON)", description = "Crea una nueva persona enviando los datos en formato JSON.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Persona creada exitosamente"),
			@ApiResponse(responseCode = "409", description = "Datos duplicados"),
			@ApiResponse(responseCode = "400", description = "Error al crear persona") })
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> crearPersonaJSON(@RequestBody PersonaDTO nueva) {
		int status = personaService.create(nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>(nueva.getTipoUsuario() + " creado exitosamente", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("Nombre de usuario duplicado", HttpStatus.CONFLICT);
		case 2:
			return new ResponseEntity<>("Correo ya registrado", HttpStatus.CONFLICT);
		case 3:
			return new ResponseEntity<>("Documento ya registrado", HttpStatus.CONFLICT);
		case 5:
			return new ResponseEntity<>("Los administradores deben usar un correo @unbosque.edu.co",
					HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error al crear persona", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Crea una nueva persona recibiendo los datos como parámetros de consulta en la URL.
	 *
	 * @param nombre      nombre de usuario
	 * @param documento   número de documento de identidad
	 * @param correo      correo electrónico
	 * @param contrasena  contraseña
	 * @param tipoUsuario rol asignado a la persona
	 * @return {@link ResponseEntity} con código 201 si es exitoso, 409 si hay datos
	 *         duplicados, o 400 si ocurre un error de validación
	 */
	@Operation(summary = "Crear persona (parámetros)", description = "Crea una nueva persona usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearPersona(@RequestParam String nombre, @RequestParam String documento,
			@RequestParam String correo, @RequestParam String contrasena, @RequestParam TipoUsuario tipoUsuario) {

		PersonaDTO nueva = new PersonaDTO(nombre, documento, correo, contrasena, tipoUsuario);
		int status = personaService.create(nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>(nueva.getTipoUsuario() + " creado exitosamente", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("Nombre de usuario duplicado", HttpStatus.CONFLICT);
		case 2:
			return new ResponseEntity<>("Correo duplicado", HttpStatus.CONFLICT);
		case 3:
			return new ResponseEntity<>("Documento duplicado", HttpStatus.CONFLICT);
		case 5:
			return new ResponseEntity<>("Los administradores deben usar un correo @unbosque.edu.co",
					HttpStatus.BAD_REQUEST);

		default:
			return new ResponseEntity<>("Error al crear persona", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Retorna la lista de todas las personas registradas en el sistema.
	 *
	 * @return {@link ResponseEntity} con la lista y código 202, o 204 si no hay registros
	 */
	@Operation(summary = "Obtener todas las personas", description = "Retorna todas las personas registradas.")
	@GetMapping("/getall")
	public ResponseEntity<List<PersonaDTO>> obtenerTodas() {
		List<PersonaDTO> lista = personaService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	/**
	 * Retorna la lista de todas las personas con rol ADMINISTRADOR.
	 *
	 * @return {@link ResponseEntity} con la lista y código 202, o 204 si no hay registros
	 */
	@Operation(summary = "Obtener administradores", description = "Retorna todas las personas con rol ADMINISTRADOR.")
	@GetMapping("/getall/administradores")
	public ResponseEntity<List<PersonaDTO>> obtenerAdmins() {
		List<PersonaDTO> lista = personaService.getAllAdmin();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	/**
	 * Retorna la lista de todas las personas con rol USUARIO.
	 *
	 * @return {@link ResponseEntity} con la lista y código 202, o 204 si no hay registros
	 */
	@Operation(summary = "Obtener usuarios", description = "Retorna todas las personas con rol USUARIO.")
	@GetMapping("/getall/usuarios")
	public ResponseEntity<List<PersonaDTO>> obtenerUsuarios() {
		List<PersonaDTO> lista = personaService.getAllUsuario();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	/**
	 * Retorna la lista de todas las personas con rol NINGUNO.
	 *
	 * @return {@link ResponseEntity} con la lista y código 202, o 204 si no hay registros
	 */
	@Operation(summary = "Obtener usuarios sin rol", description = "Retorna todas las personas con rol NINGUNO.")
	@GetMapping("/getall/ninguno")
	public ResponseEntity<List<PersonaDTO>> obtenerNinguno() {
		List<PersonaDTO> lista = personaService.getAllNinguno();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	/**
	 * Busca y retorna una persona según su ID.
	 *
	 * @param id identificador de la persona a buscar
	 * @return {@link ResponseEntity} con la persona y código 202, o 404 si no existe
	 */
	@Operation(summary = "Obtener persona por ID", description = "Busca una persona mediante su ID.")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
		PersonaDTO encontrada = personaService.getById(id);

		if (encontrada != null) {
			return new ResponseEntity<>(encontrada, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
	}

	/**
	 * Actualiza los datos de una persona identificada por su ID,
	 * recibiendo los nuevos datos en formato JSON.
	 *
	 * @param id    identificador de la persona a actualizar
	 * @param nueva objeto con los nuevos datos de la persona
	 * @return {@link ResponseEntity} con código 202 si es exitoso, 409 si hay datos
	 *         duplicados, 404 si no existe, o 400 si ocurre un error de validación
	 */
	@Operation(summary = "Actualizar persona (JSON)", description = "Actualiza una persona enviando JSON.")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarJSON(@RequestParam Long id, @RequestBody PersonaDTO nueva) {
		int status = personaService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Persona actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Nombre de usuario duplicado", HttpStatus.CONFLICT);
		case 2:
			return new ResponseEntity<>("Correo ya registrado", HttpStatus.CONFLICT);
		case 3:
			return new ResponseEntity<>("Documento ya registrado", HttpStatus.CONFLICT);
		case 4:
			return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
		case 6:
			return new ResponseEntity<>("Los administradores deben usar un correo @unbosque.edu.co",
					HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error al actualizar", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza los datos de una persona identificada por su ID,
	 * recibiendo los nuevos datos como parámetros de consulta en la URL.
	 *
	 * @param id          identificador de la persona a actualizar
	 * @param nombre      nuevo nombre de usuario
	 * @param documento   nuevo número de documento
	 * @param correo      nuevo correo electrónico
	 * @param contrasena  nueva contraseña
	 * @param tipoUsuario nuevo rol asignado
	 * @return {@link ResponseEntity} con código 202 si es exitoso, 409 si hay datos
	 *         duplicados, 404 si no existe, o 400 si ocurre un error de validación
	 */
	@Operation(summary = "Actualizar persona (parámetros)", description = "Actualiza una persona usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarPersona(@RequestParam Long id, @RequestParam String nombre,
			@RequestParam String documento, @RequestParam String correo, @RequestParam String contrasena,
			@RequestParam TipoUsuario tipoUsuario) {

		PersonaDTO nueva = new PersonaDTO(nombre, documento, correo, contrasena, tipoUsuario);
		int status = personaService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Persona actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Nombre de usuario duplicado", HttpStatus.CONFLICT);
		case 2:
			return new ResponseEntity<>("Correo ya registrado", HttpStatus.CONFLICT);
		case 3:
			return new ResponseEntity<>("Documento ya registrado", HttpStatus.CONFLICT);
		case 4:
			return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
		case 6:
			return new ResponseEntity<>("Los administradores deben usar un correo @unbosque.edu.co",
					HttpStatus.BAD_REQUEST);
		default:
			return new ResponseEntity<>("Error al actualizar", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina una persona del sistema según su ID.
	 *
	 * @param id identificador de la persona a eliminar
	 * @return {@link ResponseEntity} con código 202 si es exitoso, o 404 si no existe
	 */
	@Operation(summary = "Eliminar persona", description = "Elimina una persona por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarPersona(@PathVariable Long id) {
		int status = personaService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Persona eliminada exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
	}

	/**
	 * Retorna el total de personas registradas en el sistema.
	 *
	 * @return {@link ResponseEntity} con el conteo y código 202, o 204 si no hay registros
	 */
	@Operation(summary = "Contar personas", description = "Cuenta el total de personas.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = personaService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	/**
	 * Verifica si existe una persona registrada con el ID indicado.
	 *
	 * @param id identificador de la persona a verificar
	 * @return {@link ResponseEntity} con {@code true} y código 202 si existe,
	 *         o {@code false} y código 404 si no existe
	 */
	@Operation(summary = "Verificar existencia", description = "Verifica si existe una persona.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = personaService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}

	/**
	 * Busca y retorna una persona según su correo electrónico.
	 *
	 * @param correo correo electrónico de la persona a buscar
	 * @return {@link ResponseEntity} con la persona y código 202, o 404 si no existe
	 */
	@GetMapping("/getbycorreo")
	public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo) {
		PersonaDTO found = personaService.getByCorreo(correo);

		if (found == null) {
			return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
	}
	
	@PutMapping(path = "/mi-cuenta", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarMiCuenta(
			@RequestBody PersonaDTO nueva,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correoToken = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correoToken == null) {
			return new ResponseEntity<>("Sesión inválida. Vuelve a iniciar sesión.", HttpStatus.UNAUTHORIZED);
		}

		PersonaDTO actual = personaService.getByCorreo(correoToken);

		if (actual == null || actual.getId() == null) {
			return new ResponseEntity<>("No se encontró la cuenta del usuario.", HttpStatus.NOT_FOUND);
		}

		nueva.setCorreo(correoToken);
		nueva.setTipoUsuario(actual.getTipoUsuario());

		int status = personaService.updateById(actual.getId(), nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Cuenta actualizada exitosamente", HttpStatus.ACCEPTED);
		case 2:
			return new ResponseEntity<>("Correo ya registrado", HttpStatus.CONFLICT);
		case 3:
			return new ResponseEntity<>("Documento ya registrado", HttpStatus.CONFLICT);
		case 4:
			return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar la cuenta", HttpStatus.BAD_REQUEST);
		}
	}

	private String obtenerCorreoDesdeHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}

		try {
			String token = authorizationHeader.substring(7);
			return jwtUtil.extractUsername(token);
		} catch (Exception e) {
			return null;
		}
	}

}