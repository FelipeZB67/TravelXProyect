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

@RestController
@RequestMapping("/visa")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Consulta de Visa", description = "Endpoints para consultar si un viajero requiere visa")
public class VisaRequirementController {

	@Autowired
	private VisaRequirementService visaRequirementService;
	public VisaRequirementController() {
		// TODO Auto-generated constructor stub
	}

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