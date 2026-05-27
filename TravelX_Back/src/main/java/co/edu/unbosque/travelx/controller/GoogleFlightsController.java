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

/**
 * Controlador REST para la consulta de vuelos mediante Google Flights,
 * delegando la lógica al servicio {@link GoogleFlightsService}.
 */
@RestController
@RequestMapping("/google-flights")
@Tag(name = "Consulta de Google Flights", description = "Endpoints para consultar vuelos desde Google Flights RapidAPI")
public class GoogleFlightsController {

	@Autowired
	private GoogleFlightsService googleFlightsService;

	public GoogleFlightsController() {
	}

	/**
	 * Consulta vuelos disponibles recibiendo los parámetros de búsqueda
	 * encapsulados en un objeto JSON en el cuerpo de la petición.
	 *
	 * @param request objeto con los filtros de búsqueda, incluyendo códigos IATA de origen y destino
	 * @return {@link ResponseEntity} con los resultados y código 202 si la consulta es exitosa,
	 *         o 400 si la respuesta es nula o indica fallo
	 */
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