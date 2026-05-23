package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.GoogleFlightsSearchDTO;
import co.edu.unbosque.travelx.dto.GoogleFlightsSearchRequestDTO;
import co.edu.unbosque.travelx.service.GoogleFlightsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/google-flights")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Consulta de Google Flights", description = "Endpoints para consultar vuelos desde Google Flights RapidAPI")
public class GoogleFlightsController {

	@Autowired
	private GoogleFlightsService googleFlightsService;

	public GoogleFlightsController() {
	}

	@Operation(summary = "Buscar vuelos", description = "Consulta vuelos usando codigos IATA de aeropuerto.")
	@PostMapping(path = "/searchjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GoogleFlightsSearchDTO> buscarVuelosJSON(@RequestBody GoogleFlightsSearchRequestDTO request) {
		GoogleFlightsSearchDTO respuesta = googleFlightsService.searchFlights(request);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}
}