package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.service.ReservaService;

@RestController
@RequestMapping("/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

	private final ReservaService reservaService;

	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}

	@PostMapping("/agregar")
	public ResponseEntity<Reserva> agregarReserva(@RequestBody TravelOptionDTO option, Authentication auth) {
		Reserva reserva = reservaService.crearReserva(auth.getName(), option);
		return ResponseEntity.ok(reserva);
	}

	@GetMapping("/mis-reservas")
	public ResponseEntity<List<Reserva>> misReservas(Authentication auth) {
		return ResponseEntity.ok(reservaService.misReservas(auth.getName()));
	}

	@GetMapping(value = "/{id}/imprimir", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> imprimir(@PathVariable Long id, Authentication auth) {
		Reserva reserva = reservaService.buscarPropia(id, auth.getName());

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
}