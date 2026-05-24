package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;
import co.edu.unbosque.travelx.service.TerrestrialTransportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la consulta de rutas de transporte terrestre,
 * delegando la lógica al servicio {@link TerrestrialTransportService}.
 */
@RestController
@RequestMapping("/terrestrial")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Transporte Terrestre", description = "Endpoints para consultar rutas terrestres desde MockAPI")
public class TerrestrialTransportController {

	@Autowired
	private TerrestrialTransportService terrestrialTransportService;

	public TerrestrialTransportController() {
	}

	/**
	 * Consulta una ruta terrestre disponible según los datos de origen y destino
	 * recibidos en formato JSON.
	 *
	 * @param request objeto con la ciudad y país de origen y destino
	 * @return {@link ResponseEntity} con la ruta encontrada y código 202 si está disponible,
	 *         o 400 si la respuesta es nula o no hay disponibilidad
	 */
	@Operation(summary = "Buscar ruta terrestre", description = "Consulta una ruta terrestre por ciudad y pais de origen y destino.")
	@PostMapping(path = "/searchjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TravelOptionDTO> buscarRutaTerrestre(@RequestBody TravelSearchRequestDTO request) {
		TravelOptionDTO response = terrestrialTransportService.searchRoute(request);

		if (response == null || Boolean.FALSE.equals(response.getAvailable())) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}