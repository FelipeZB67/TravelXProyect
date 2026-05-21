package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.ReservaInternacionalDTO;
import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;
import co.edu.unbosque.travelx.service.ReservaInternacionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reservainternacional")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Gestión de Reservas Internacionales", description = "Endpoints para administrar reservas internacionales")
@SecurityRequirement(name = "bearerAuth")
public class ReservaInternacionalController {

	@Autowired
	private ReservaInternacionalService reservaInternacionalService;

	public ReservaInternacionalController() {
	}

	@Operation(summary = "Crear reserva internacional (JSON)", description = "Crea una nueva reserva internacional enviando los datos en formato JSON.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Reserva internacional creada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Error al crear reserva internacional")
	})
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> crearReservaInternacionalJSON(@RequestBody ReservaInternacionalDTO nueva) {
		int status = reservaInternacionalService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Reserva internacional creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear reserva internacional", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Crear reserva internacional (parámetros)", description = "Crea una nueva reserva internacional usando parámetros.")
	@PostMapping("/create")
	public ResponseEntity<String> crearReservaInternacional(
			@RequestParam String ciudadOrigen,
			@RequestParam String ciudadDestino,
			@RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin,
			@RequestParam String hotel,
			@RequestParam Double precioHospedaje,
			@RequestParam Double precioTransporte,
			@RequestParam MetodoTransporte metodoTransporte,
			@RequestParam String paisOrigen,
			@RequestParam String paisDestino,
			@RequestParam boolean requiereVisa) {

		ReservaInternacionalDTO nueva = new ReservaInternacionalDTO();

		nueva.setCiudadOrigen(ciudadOrigen);
		nueva.setCiudadDestino(ciudadDestino);
		nueva.setFechaInicio(fechaInicio);
		nueva.setFechaFin(fechaFin);
		nueva.setHotel(hotel);
		nueva.setPrecioHospedaje(precioHospedaje);
		nueva.setPrecioTransporte(precioTransporte);
		nueva.setMetodoTransporte(metodoTransporte);
		nueva.setPaisOrigen(paisOrigen);
		nueva.setPaisDestino(paisDestino);
		nueva.setRequiereVisa(requiereVisa);

		int status = reservaInternacionalService.create(nueva);

		if (status == 0) {
			return new ResponseEntity<>("Reserva internacional creada exitosamente", HttpStatus.CREATED);
		}

		return new ResponseEntity<>("Error al crear reserva internacional", HttpStatus.BAD_REQUEST);
	}

	@Operation(summary = "Obtener todas las reservas internacionales", description = "Retorna todas las reservas internacionales registradas.")
	@GetMapping("/getall")
	public ResponseEntity<List<ReservaInternacionalDTO>> obtenerTodas() {
		List<ReservaInternacionalDTO> lista = reservaInternacionalService.getAll();

		if (lista.isEmpty()) {
			return new ResponseEntity<>(lista, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(lista, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Actualizar reserva internacional (JSON)", description = "Actualiza una reserva internacional enviando JSON.")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> actualizarJSON(@RequestParam Long id, @RequestBody ReservaInternacionalDTO nueva) {
		int status = reservaInternacionalService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Reserva internacional actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Reserva internacional no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar reserva internacional", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Actualizar reserva internacional (parámetros)", description = "Actualiza una reserva internacional usando parámetros.")
	@PutMapping("/update")
	public ResponseEntity<String> actualizarReservaInternacional(
			@RequestParam Long id,
			@RequestParam String ciudadOrigen,
			@RequestParam String ciudadDestino,
			@RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin,
			@RequestParam String hotel,
			@RequestParam Double precioHospedaje,
			@RequestParam Double precioTransporte,
			@RequestParam MetodoTransporte metodoTransporte,
			@RequestParam String paisOrigen,
			@RequestParam String paisDestino,
			@RequestParam boolean requiereVisa) {

		ReservaInternacionalDTO nueva = new ReservaInternacionalDTO();

		nueva.setCiudadOrigen(ciudadOrigen);
		nueva.setCiudadDestino(ciudadDestino);
		nueva.setFechaInicio(fechaInicio);
		nueva.setFechaFin(fechaFin);
		nueva.setHotel(hotel);
		nueva.setPrecioHospedaje(precioHospedaje);
		nueva.setPrecioTransporte(precioTransporte);
		nueva.setMetodoTransporte(metodoTransporte);
		nueva.setPaisOrigen(paisOrigen);
		nueva.setPaisDestino(paisDestino);
		nueva.setRequiereVisa(requiereVisa);

		int status = reservaInternacionalService.updateById(id, nueva);

		switch (status) {
		case 0:
			return new ResponseEntity<>("Reserva internacional actualizada exitosamente", HttpStatus.ACCEPTED);
		case 1:
			return new ResponseEntity<>("Reserva internacional no encontrada", HttpStatus.NOT_FOUND);
		default:
			return new ResponseEntity<>("Error al actualizar reserva internacional", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar reserva internacional", description = "Elimina una reserva internacional por ID.")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> eliminarReservaInternacional(@PathVariable Long id) {
		int status = reservaInternacionalService.deleteById(id);

		if (status == 0) {
			return new ResponseEntity<>("Reserva internacional eliminada exitosamente", HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>("Reserva internacional no encontrada", HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Contar reservas internacionales", description = "Cuenta el total de reservas internacionales.")
	@GetMapping("/count")
	public ResponseEntity<Long> contar() {
		Long total = reservaInternacionalService.count();

		if (total == 0) {
			return new ResponseEntity<>(total, HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(total, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Verificar existencia", description = "Verifica si existe una reserva internacional por ID.")
	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> existe(@PathVariable Long id) {
		boolean found = reservaInternacionalService.exist(id);

		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
	}
}