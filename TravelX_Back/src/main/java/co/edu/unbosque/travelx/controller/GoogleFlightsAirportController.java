package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.AirportCodeDTO;
import co.edu.unbosque.travelx.service.GoogleFlightsAirportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/google-flights-airport")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Google Flights Airport", description = "Endpoints para buscar codigos IATA dinamicamente")
public class GoogleFlightsAirportController {

	@Autowired
	private GoogleFlightsAirportService googleFlightsAirportService;

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