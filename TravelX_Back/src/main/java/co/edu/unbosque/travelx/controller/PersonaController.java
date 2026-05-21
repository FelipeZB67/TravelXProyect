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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/persona")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Gestión de Personas", description = "Endpoints para administrar personas")
@SecurityRequirement(name = "bearerAuth")
public class PersonaController {

	@Autowired
	private PersonaService personaService;

	public PersonaController() {
	}

	@Operation(summary = "Crear persona (JSON)", description = "Crea una nueva persona enviando los datos en formato JSON.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Persona creada exitosamente"),
			@ApiResponse(responseCode = "409", description = "Datos duplicados"),
			@ApiResponse(responseCode = "400", description = "Error al crear persona")
	})
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

	@Operation(summary = "Crear persona (parámetros)", description = "Crea una nueva persona usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearPersona(
			@RequestParam String nombre,
			@RequestParam String documento,
			@RequestParam String correo,
			@RequestParam String contrasena,
			@RequestParam TipoUsuario tipoUsuario) {

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

	@Operation(summary = "Obtener todas las personas", description = "Retorna todas las personas registradas.")
	@GetMapping("/getall")
	public ResponseEntity<List<PersonaDTO>> obtenerTodas() {
		List<PersonaDTO> lista = personaService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Obtener administradores", description = "Retorna todas las personas con rol ADMINISTRADOR.")
	@GetMapping("/getall/administradores")
	public ResponseEntity<List<PersonaDTO>> obtenerAdmins() {
		List<PersonaDTO> lista = personaService.getAllAdmin();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Obtener usuarios", description = "Retorna todas las personas con rol USUARIO.")
	@GetMapping("/getall/usuarios")
	public ResponseEntity<List<PersonaDTO>> obtenerUsuarios() {
		List<PersonaDTO> lista = personaService.getAllUsuario();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Obtener usuarios sin rol", description = "Retorna todas las personas con rol NINGUNO.")
	@GetMapping("/getall/ninguno")
	public ResponseEntity<List<PersonaDTO>> obtenerNinguno() {
		List<PersonaDTO> lista = personaService.getAllNinguno();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Obtener persona por ID", description = "Busca una persona mediante su ID.")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
		PersonaDTO encontrada = personaService.getById(id);

		if (encontrada != null) {
			return new ResponseEntity<>(encontrada, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
	}

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

	@Operation(summary = "Actualizar persona (parámetros)", description = "Actualiza una persona usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarPersona(
			@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String documento,
			@RequestParam String correo,
			@RequestParam String contrasena,
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

	@Operation(summary = "Eliminar persona", description = "Elimina una persona por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarPersona(@PathVariable Long id) {
		int status = personaService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Persona eliminada exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Contar personas", description = "Cuenta el total de personas.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = personaService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Verificar existencia", description = "Verifica si existe una persona.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = personaService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}
	@GetMapping("/getbycorreo")
	public ResponseEntity<?> obtenerPorCorreo(@RequestParam String correo) {
		PersonaDTO found = personaService.getByCorreo(correo);

		if (found == null) {
			return new ResponseEntity<>("Persona no encontrada", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
	}
	
}