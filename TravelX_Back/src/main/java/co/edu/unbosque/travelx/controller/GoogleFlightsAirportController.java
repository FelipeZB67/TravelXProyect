package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.AirportCodeDTO;
import co.edu.unbosque.travelx.service.GoogleFlightsAirportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la búsqueda dinámica de aeropuertos y sus códigos IATA,
 * delegando la lógica al servicio {@link GoogleFlightsAirportService}.
 */
@RestController
@RequestMapping("/google-flights-airport")
@Tag(name = "Google Flights Airport", description = "Endpoints para buscar codigos IATA dinamicamente")
public class GoogleFlightsAirportController {

	@Autowired
	private GoogleFlightsAirportService googleFlightsAirportService;

	/**
	 * Busca un aeropuerto según el nombre o ciudad y el código de país,
	 * retornando su código IATA si es encontrado.
	 *
	 * @param query       nombre o ciudad del aeropuerto a buscar
	 * @param countryCode código del país donde se ubica el aeropuerto
	 * @return {@link ResponseEntity} con el código IATA y código 202 si se encuentra,
	 *         o 404 si no se hallaron resultados
	 */
	@Operation(summary = "Buscar aeropuerto", description = "Busca un aeropuerto y retorna su codigo IATA.")
	@GetMapping("/search")
	public ResponseEntity<AirportCodeDTO> searchAirport(
			@RequestParam String query,
			@RequestParam String countryCode) {

		AirportCodeDTO response = googleFlightsAirportService.searchAirport(query, countryCode);

		if (Boolean.FALSE.equals(response.getFound())) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}