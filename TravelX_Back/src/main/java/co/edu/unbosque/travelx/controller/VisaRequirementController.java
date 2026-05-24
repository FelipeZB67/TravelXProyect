package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.VisaRequirementDTO;
import co.edu.unbosque.travelx.service.VisaRequirementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la consulta de requisitos de visa,
 * delegando la lógica al servicio {@link VisaRequirementService}.
 */
@RestController
@RequestMapping("/visa")
@Tag(name = "Consulta de Visa", description = "Endpoints para consultar si un viajero requiere visa")
public class VisaRequirementController {

	@Autowired
	private VisaRequirementService visaRequirementService;
	public VisaRequirementController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Consulta si un viajero requiere visa según su país de pasaporte,
	 * el país de destino y la fecha de viaje.
	 *
	 * @param pcc   código del país del pasaporte del viajero
	 * @param dcc   código del país de destino
	 * @param fecha fecha prevista del viaje
	 * @return {@link ResponseEntity} con el resultado de la consulta y código 202 si es exitoso,
	 *         o 400 si la respuesta es nula o indica fallo
	 */
	@Operation(summary = "Consultar requisito de visa", description = "Consulta si se requiere visa segun pais de pasaporte, pais destino y fecha de viaje.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Consulta realizada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Parametros invalidos o error al consultar la API externa")
	})
	@GetMapping("/check/{pcc}/{dcc}/{fecha}")
	public ResponseEntity<VisaRequirementDTO> consultarVisa(
			@PathVariable String pcc,
			@PathVariable String dcc,
			@PathVariable LocalDate fecha) {

		VisaRequirementDTO respuesta = visaRequirementService.checkVisaRequirement(pcc, dcc, fecha);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getProviderSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}
}