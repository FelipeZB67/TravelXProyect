package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.NominatimResolvedLocationDTO;
import co.edu.unbosque.travelx.service.NominatimService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/nominatim")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Nominatim", description = "Endpoints para resolver ciudades usando OpenStreetMap")
public class NominatimController {

	@Autowired
	private NominatimService nominatimService;

	@Operation(summary = "Buscar ciudad", description = "Resuelve ciudad y pais a latitud, longitud y datos normalizados.")
	@GetMapping("/search")
	public ResponseEntity<NominatimResolvedLocationDTO> search(
			@RequestParam String city,
			@RequestParam String country) {

		NominatimResolvedLocationDTO response = nominatimService.searchCity(city, country);

		if (Boolean.FALSE.equals(response.getFound())) {
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}