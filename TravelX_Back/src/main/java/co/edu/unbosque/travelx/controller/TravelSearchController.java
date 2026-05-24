package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;
import co.edu.unbosque.travelx.dto.TravelSearchResponseDTO;
import co.edu.unbosque.travelx.service.TravelSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/travel-search")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Busqueda de Viajes", description = "Endpoint unificado para el front")
public class TravelSearchController {

	@Autowired
	private TravelSearchService travelSearchService;

	public TravelSearchController() {
	}

	@Operation(summary = "Buscar opciones de viaje", description = "Consulta vuelos, Airbnb y hoteles usando un request estandarizado para el front.")
	@PostMapping(path = "/searchjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TravelSearchResponseDTO> buscarViaje(@RequestBody TravelSearchRequestDTO request) {
		TravelSearchResponseDTO response = travelSearchService.search(request);

		if (response == null || Boolean.FALSE.equals(response.getSuccess())) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	@Operation(summary = "Buscar transporte", description = "Consulta opciones de transporte: vuelos y transporte terrestre.")
	@PostMapping(path = "/transportjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TravelSearchResponseDTO> buscarTransporte(@RequestBody TravelSearchRequestDTO request) {
		request.setIncluirVuelos(true);
		request.setIncluirTransporteTerrestre(true);
		request.setIncluirAirbnb(false);
		request.setIncluirHoteles(false);

		TravelSearchResponseDTO response = travelSearchService.search(request);

		if (response == null || Boolean.FALSE.equals(response.getSuccess())) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Buscar hospedaje", description = "Consulta opciones de hospedaje: Airbnb y hoteles.")
	@PostMapping(path = "/lodgingjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TravelSearchResponseDTO> buscarHospedaje(@RequestBody TravelSearchRequestDTO request) {
		request.setIncluirVuelos(false);
		request.setIncluirTransporteTerrestre(false);
		request.setIncluirAirbnb(true);
		request.setIncluirHoteles(false);

		TravelSearchResponseDTO response = travelSearchService.search(request);

		if (response == null || Boolean.FALSE.equals(response.getSuccess())) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}