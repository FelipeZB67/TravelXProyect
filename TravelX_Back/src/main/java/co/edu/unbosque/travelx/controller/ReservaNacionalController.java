package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.ReservaNacionalDTO;
import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;
import co.edu.unbosque.travelx.service.ReservaNacionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reservanacional")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Gestión de Reservas Nacionales", description = "Endpoints para administrar reservas nacionales")
@SecurityRequirement(name = "bearerAuth")
public class ReservaNacionalController {

	@Autowired
	private ReservaNacionalService reservaNacionalService;

	public ReservaNacionalController() {
	}

	@Operation(summary = "Crear reserva nacional (JSON)", description = "Crea una nueva reserva nacional enviando los datos en formato JSON.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Reserva nacional creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al crear reserva nacional")
	})
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> crearReservaNacionalJSON(@RequestBody ReservaNacionalDTO nueva) {
		int status = reservaNacionalService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Reserva nacional creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear reserva nacional", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Crear reserva nacional (parámetros)", description = "Crea una nueva reserva nacional usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearReservaNacional(
			@RequestParam String ciudadOrigen,
			@RequestParam String ciudadDestino,
			@RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin,
			@RequestParam String hotel,
			@RequestParam Double precioHospedaje,
			@RequestParam Double precioTransporte,
			@RequestParam MetodoTransporte metodoTransporte) {

		ReservaNacionalDTO nueva = new ReservaNacionalDTO();

		nueva.setCiudadOrigen(ciudadOrigen);
		nueva.setCiudadDestino(ciudadDestino);
		nueva.setFechaInicio(fechaInicio);
		nueva.setFechaFin(fechaFin);
		nueva.setHotel(hotel);
		nueva.setPrecioHospedaje(precioHospedaje);
		nueva.setPrecioTransporte(precioTransporte);
		nueva.setMetodoTransporte(metodoTransporte);

		int status = reservaNacionalService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Reserva nacional creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear reserva nacional", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Obtener todas las reservas nacionales", description = "Retorna todas las reservas nacionales registradas.")
	@GetMapping("/getall")
	public ResponseEntity<List<ReservaNacionalDTO>> obtenerTodas() {
		List<ReservaNacionalDTO> lista = reservaNacionalService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Actualizar reserva nacional (JSON)", description = "Actualiza una reserva nacional enviando JSON.")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarJSON(@RequestParam Long id, @RequestBody ReservaNacionalDTO nueva) {
		int status = reservaNacionalService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Reserva nacional actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Reserva nacional no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar reserva nacional", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Actualizar reserva nacional (parámetros)", description = "Actualiza una reserva nacional usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarReservaNacional(
			@RequestParam Long id,
			@RequestParam String ciudadOrigen,
			@RequestParam String ciudadDestino,
			@RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin,
			@RequestParam String hotel,
			@RequestParam Double precioHospedaje,
			@RequestParam Double precioTransporte,
			@RequestParam MetodoTransporte metodoTransporte) {

		ReservaNacionalDTO nueva = new ReservaNacionalDTO();

		nueva.setCiudadOrigen(ciudadOrigen);
		nueva.setCiudadDestino(ciudadDestino);
		nueva.setFechaInicio(fechaInicio);
		nueva.setFechaFin(fechaFin);
		nueva.setHotel(hotel);
		nueva.setPrecioHospedaje(precioHospedaje);
		nueva.setPrecioTransporte(precioTransporte);
		nueva.setMetodoTransporte(metodoTransporte);

		int status = reservaNacionalService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Reserva nacional actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Reserva nacional no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar reserva nacional", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar reserva nacional", description = "Elimina una reserva nacional por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarReservaNacional(@PathVariable Long id) {
		int status = reservaNacionalService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Reserva nacional eliminada exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Reserva nacional no encontrada", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Contar reservas nacionales", description = "Cuenta el total de reservas nacionales.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = reservaNacionalService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Verificar existencia", description = "Verifica si existe una reserva nacional por ID.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = reservaNacionalService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}
}