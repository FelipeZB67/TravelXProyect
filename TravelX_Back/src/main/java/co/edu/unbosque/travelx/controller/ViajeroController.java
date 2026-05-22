package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.service.ViajeroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/viajero")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Gestión de Viajeros", description = "Endpoints para administrar viajeros")
@SecurityRequirement(name = "bearerAuth")
public class ViajeroController {

	@Autowired
	private ViajeroService viajeroService;

	public ViajeroController() {
	}

	@Operation(summary = "Crear viajero (JSON)", description = "Crea un nuevo viajero enviando los datos en formato JSON.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Viajero creado exitosamente"),
			@ApiResponse(responseCode = "409", description = "Pasaporte ya registrado"),
			@ApiResponse(responseCode = "400", description = "Error al crear viajero")
	})
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> crearViajeroJSON(@RequestBody ViajeroDTO nuevo) {
		int status = viajeroService.create(nuevo);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Viajero creado exitosamente", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("Pasaporte ya registrado", HttpStatus.CONFLICT);
		default:
			return new ResponseEntity<>("Error al crear viajero", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Crear viajero (parámetros)", description = "Crea un nuevo viajero usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearViajero(
			@RequestParam String nombre,
			@RequestParam String pasaporte,
			@RequestParam LocalDate fechaNacimiento,
			@RequestParam boolean esMayor) {

		ViajeroDTO nuevo = new ViajeroDTO(nombre, pasaporte, fechaNacimiento, esMayor);
		int status = viajeroService.create(nuevo);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Viajero creado exitosamente", HttpStatus.CREATED);
		case 1:
			return new ResponseEntity<>("Pasaporte ya registrado", HttpStatus.CONFLICT);
		default:
			return new ResponseEntity<>("Error al crear viajero", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Obtener todos los viajeros", description = "Retorna todos los viajeros registrados.")
	@GetMapping("/getall")
	public ResponseEntity<List<ViajeroDTO>> obtenerTodos() {
		List<ViajeroDTO> lista = viajeroService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Obtener viajero por ID", description = "Busca un viajero mediante su ID.")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
		ViajeroDTO encontrado = viajeroService.getById(id);

		if (encontrado != null) {
			return new ResponseEntity<>(encontrado, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Viajero no encontrado", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Actualizar viajero (JSON)", description = "Actualiza un viajero enviando JSON.")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarJSON(@RequestParam Long id, @RequestBody ViajeroDTO nuevo) {
		int status = viajeroService.updateById(id, nuevo);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Viajero actualizado exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Viajero no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Pasaporte ya registrado", HttpStatus.CONFLICT);
		default:
			return new ResponseEntity<>("Error al actualizar viajero", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Actualizar viajero (parámetros)", description = "Actualiza un viajero usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarViajero(
			@RequestParam Long id,
			@RequestParam String nombre,
			@RequestParam String pasaporte,
			@RequestParam LocalDate fechaNacimiento,
			@RequestParam boolean esMayor) {

		ViajeroDTO nuevo = new ViajeroDTO(nombre, pasaporte, fechaNacimiento, esMayor);
		int status = viajeroService.updateById(id, nuevo);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Viajero actualizado exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Viajero no encontrado", HttpStatus.NOT_FOUND);
		case 2:
			return new ResponseEntity<>("Pasaporte ya registrado", HttpStatus.CONFLICT);
		default:
			return new ResponseEntity<>("Error al actualizar viajero", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar viajero", description = "Elimina un viajero por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarViajero(@PathVariable Long id) {
		int status = viajeroService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Viajero eliminado exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Viajero no encontrado", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Contar viajeros", description = "Cuenta el total de viajeros.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = viajeroService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Verificar existencia", description = "Verifica si existe un viajero por ID.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = viajeroService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Verificar pasaporte", description = "Verifica si un pasaporte ya está registrado.")
	@GetMapping("/exists/pasaporte")
	public ResponseEntity<Boolean> existePasaporte(@RequestParam String pasaporte) {
		boolean found = viajeroService.findPasaporteAlreadyTaken(pasaporte);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(false, HttpStatus.ACCEPTED);
	}
}