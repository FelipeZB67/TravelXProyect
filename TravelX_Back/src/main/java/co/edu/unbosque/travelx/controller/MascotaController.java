package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.MascotaDTO;
import co.edu.unbosque.travelx.service.MascotaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/mascota")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Gestión de Mascotas", description = "Endpoints para administrar mascotas")
@SecurityRequirement(name = "bearerAuth")
public class MascotaController {

	@Autowired
	private MascotaService mascotaService;

	public MascotaController() {
	}

	@Operation(summary = "Crear mascota (JSON)", description = "Crea una nueva mascota enviando los datos en formato JSON.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Mascota creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al crear mascota")
	})
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> crearMascotaJSON(@RequestBody MascotaDTO nueva) {
		int status = mascotaService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Mascota creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear mascota", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Crear mascota (parámetros)", description = "Crea una nueva mascota usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearMascota(
			@RequestParam String especie,
			@RequestParam String raza,
			@RequestParam LocalDate fechaNacimiento) {

		MascotaDTO nueva = new MascotaDTO(especie, raza, fechaNacimiento);
		int status = mascotaService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Mascota creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear mascota", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Obtener todas las mascotas", description = "Retorna todas las mascotas registradas.")
	@GetMapping("/getall")
	public ResponseEntity<List<MascotaDTO>> obtenerTodas() {
		List<MascotaDTO> lista = mascotaService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Actualizar mascota (JSON)", description = "Actualiza una mascota enviando JSON.")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarJSON(@RequestParam Long id, @RequestBody MascotaDTO nueva) {
		int status = mascotaService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Mascota actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Mascota no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar mascota", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Actualizar mascota (parámetros)", description = "Actualiza una mascota usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarMascota(
			@RequestParam Long id,
			@RequestParam String especie,
			@RequestParam String raza,
			@RequestParam LocalDate fechaNacimiento) {

		MascotaDTO nueva = new MascotaDTO(especie, raza, fechaNacimiento);
		int status = mascotaService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Mascota actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Mascota no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar mascota", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar mascota", description = "Elimina una mascota por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarMascota(@PathVariable Long id) {
		int status = mascotaService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Mascota eliminada exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Mascota no encontrada", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Contar mascotas", description = "Cuenta el total de mascotas.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = mascotaService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Verificar existencia", description = "Verifica si existe una mascota.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = mascotaService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}
}