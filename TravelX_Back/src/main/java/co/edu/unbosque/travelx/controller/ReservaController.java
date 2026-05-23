package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.security.JwtUtil;
import co.edu.unbosque.travelx.service.ReservaService;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

	private final ReservaService reservaService;
	private final JwtUtil jwtUtil;

	public ReservaController(ReservaService reservaService, JwtUtil jwtUtil) {
		this.reservaService = reservaService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/agregar")
	public ResponseEntity<Reserva> agregarReserva(
			@RequestBody TravelOptionDTO option,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correo = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Reserva reserva = reservaService.crearReserva(correo, option);
		return ResponseEntity.ok(reserva);
	}

	@GetMapping("/mis-reservas")
	public ResponseEntity<List<Reserva>> misReservas(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correo = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(reservaService.misReservas(correo));
	}
	
	@GetMapping("/todas")
	public ResponseEntity<List<Reserva>> todasReservas() {
		return ResponseEntity.ok(reservaService.todasReservas());
	}
	
	@PostMapping("/admin/crear")
	public ResponseEntity<Reserva> crearReservaAdmin(@RequestBody Reserva reserva) {
		return ResponseEntity.ok(reservaService.crearReservaAdmin(reserva));
	}

	@PutMapping("/admin/actualizar/{id}")
	public ResponseEntity<Reserva> actualizarReservaAdmin(@PathVariable Long id, @RequestBody Reserva reserva) {
		return ResponseEntity.ok(reservaService.actualizarReservaAdmin(id, reserva));
	}

	@DeleteMapping("/admin/eliminar/{id}")
	public ResponseEntity<Void> eliminarReservaAdmin(@PathVariable Long id) {
		reservaService.eliminarReservaAdmin(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/imprimir", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> imprimir(
			@PathVariable Long id,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correo = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Reserva reserva = reservaService.buscarPropia(id, correo);

		String html = """
				<!DOCTYPE html>
				<html>
				<head>
					<meta charset="UTF-8">
					<title>Reserva</title>
				</head>
				<body onload="window.print()">
					<h1>Reserva TravelX</h1>
					<p><strong>Proveedor:</strong> %s</p>
					<p><strong>Tipo:</strong> %s</p>
					<p><strong>Titulo:</strong> %s</p>
					<p><strong>Origen:</strong> %s, %s</p>
					<p><strong>Destino:</strong> %s, %s</p>
					<p><strong>Salida:</strong> %s</p>
					<p><strong>Regreso:</strong> %s</p>
					<p><strong>Precio:</strong> %s</p>
					<p><strong>Adultos:</strong> %s</p>
					<p><strong>Ninos:</strong> %s</p>
					<p><strong>Clase:</strong> %s</p>
				</body>
				</html>
				""".formatted(reserva.getProvider(), reserva.getType(), reserva.getTitle(), reserva.getOriginCity(),
				reserva.getOriginCountry(), reserva.getDestinationCity(), reserva.getDestinationCountry(),
				reserva.getDepartureDate(), reserva.getReturnDate(), reserva.getPriceText(), reserva.getAdults(),
				reserva.getChildren(), reserva.getTravelClass());

		return ResponseEntity.ok(html);
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