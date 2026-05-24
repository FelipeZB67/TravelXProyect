package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.NominatimResolvedLocationDTO;
import co.edu.unbosque.travelx.service.NominatimService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la resolución de ciudades mediante OpenStreetMap Nominatim,
 * delegando la lógica al servicio {@link NominatimService}.
 */
@RestController
@RequestMapping("/nominatim")
@Tag(name = "Nominatim", description = "Endpoints para resolver ciudades usando OpenStreetMap")
public class NominatimController {

	@Autowired
	private NominatimService nominatimService;

	/**
	 * Resuelve el nombre de una ciudad y su país a coordenadas geográficas
	 * y datos de ubicación normalizados.
	 *
	 * @param city    nombre de la ciudad a buscar
	 * @param country país donde se ubica la ciudad
	 * @return {@link ResponseEntity} con la ubicación resuelta y código 202 si se encuentra,
	 *         o 404 si no se hallaron resultados
	 */
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