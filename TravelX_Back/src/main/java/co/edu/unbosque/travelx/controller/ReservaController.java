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

/**
 * Controlador REST para la gestión de reservas en TravelX.
 * Expone endpoints para crear, consultar, actualizar, eliminar e imprimir reservas,
 * delegando la lógica al servicio {@link ReservaService}.
 */
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

	/**
	 * Crea una nueva reserva para el usuario autenticado a partir de una opción de viaje.
	 *
	 * @param option              objeto con los datos de la opción de viaje a reservar
	 * @param authorizationHeader cabecera de autorización con el token JWT del usuario
	 * @return {@link ResponseEntity} con la reserva creada y código 200,
	 *         o 401 si el token es inválido o ausente
	 */
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

	/**
	 * Retorna la lista de reservas pertenecientes al usuario autenticado.
	 *
	 * @param authorizationHeader cabecera de autorización con el token JWT del usuario
	 * @return {@link ResponseEntity} con la lista de reservas y código 200,
	 *         o 401 si el token es inválido o ausente
	 */
	@GetMapping("/mis-reservas")
	public ResponseEntity<List<Reserva>> misReservas(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correo = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(reservaService.misReservas(correo));
	}
	
	/**
	 * Retorna la lista completa de reservas registradas en el sistema.
	 *
	 * @return {@link ResponseEntity} con todas las reservas y código 200
	 */
	@GetMapping("/todas")
	public ResponseEntity<List<Reserva>> todasReservas() {
		return ResponseEntity.ok(reservaService.todasReservas());
	}
	
	/**
	 * Crea una reserva directamente desde el panel de administración,
	 * sin validación de usuario autenticado.
	 *
	 * @param reserva objeto con los datos completos de la reserva a crear
	 * @return {@link ResponseEntity} con la reserva creada y código 200
	 */
	@PostMapping("/admin/crear")
	public ResponseEntity<Reserva> crearReservaAdmin(@RequestBody Reserva reserva) {
		return ResponseEntity.ok(reservaService.crearReservaAdmin(reserva));
	}

	/**
	 * Actualiza una reserva existente desde el panel de administración.
	 *
	 * @param id      identificador de la reserva a actualizar
	 * @param reserva objeto con los nuevos datos de la reserva
	 * @return {@link ResponseEntity} con la reserva actualizada y código 200
	 */
	@PutMapping("/admin/actualizar/{id}")
	public ResponseEntity<Reserva> actualizarReservaAdmin(@PathVariable Long id, @RequestBody Reserva reserva) {
		return ResponseEntity.ok(reservaService.actualizarReservaAdmin(id, reserva));
	}

	/**
	 * Elimina una reserva del sistema desde el panel de administración.
	 *
	 * @param id identificador de la reserva a eliminar
	 * @return {@link ResponseEntity} vacío con código 204
	 */
	@DeleteMapping("/admin/eliminar/{id}")
	public ResponseEntity<Void> eliminarReservaAdmin(@PathVariable Long id) {
		reservaService.eliminarReservaAdmin(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarReservaPropia(
			@PathVariable Long id,
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

		String correo = obtenerCorreoDesdeHeader(authorizationHeader);

		if (correo == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		reservaService.eliminarPropia(id, correo);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Genera una página HTML con el detalle de una reserva propia del usuario autenticado,
	 * lista para ser impresa desde el navegador.
	 *
	 * @param id                  identificador de la reserva a imprimir
	 * @param authorizationHeader cabecera de autorización con el token JWT del usuario
	 * @return {@link ResponseEntity} con el HTML generado y código 200,
	 *         o 401 si el token es inválido o ausente
	 */
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

	/**
	 * Extrae el correo electrónico del usuario a partir del token JWT
	 * contenido en la cabecera de autorización.
	 *
	 * @param authorizationHeader cabecera de autorización en formato {@code Bearer <token>}
	 * @return correo del usuario si el token es válido, o {@code null} si es ausente o inválido
	 */
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