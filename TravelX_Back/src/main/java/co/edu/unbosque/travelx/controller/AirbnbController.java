package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.AirbnbSearchDTO;
import co.edu.unbosque.travelx.dto.AirbnbSearchRequestDTO;
import co.edu.unbosque.travelx.service.AirbnbService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/airbnb")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Consulta de Airbnb", description = "Endpoints para consultar alojamientos tipo Airbnb desde RapidAPI")
public class AirbnbController {

	@Autowired
	private AirbnbService airbnbService;

	public AirbnbController() {
	}

	@Operation(summary = "Buscar alojamientos por Place ID", description = "Busca propiedades tipo Airbnb usando el Place ID del destino.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Consulta realizada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Parametros invalidos o error al consultar la API externa")
	})
	@GetMapping("/search")
	public ResponseEntity<AirbnbSearchDTO> buscarPorPlaceId(
			@RequestParam String placeId,
			@RequestParam(required = false) String nextPageCursor,
			@RequestParam(required = false) String checkin,
			@RequestParam(required = false) String checkout,
			@RequestParam(required = false, defaultValue = "1") Integer adults,
			@RequestParam(required = false, defaultValue = "0") Integer children,
			@RequestParam(required = false, defaultValue = "0") Integer infants,
			@RequestParam(required = false, defaultValue = "0") Integer pets,
			@RequestParam(required = false, defaultValue = "0") Integer priceMin,
			@RequestParam(required = false, defaultValue = "0") Integer priceMax,
			@RequestParam(required = false, defaultValue = "0") Integer minBedrooms,
			@RequestParam(required = false, defaultValue = "0") Integer minBeds,
			@RequestParam(required = false) String amenities,
			@RequestParam(required = false, defaultValue = "false") Boolean guestFavorite,
			@RequestParam(required = false, defaultValue = "false") Boolean ib,
			@RequestParam(required = false) String flexibleDateSearchFilterType,
			@RequestParam(required = false, defaultValue = "USD") String currency) {

		AirbnbSearchRequestDTO request = new AirbnbSearchRequestDTO();
		request.setPlaceId(placeId);
		request.setNextPageCursor(nextPageCursor);
		request.setCheckin(checkin);
		request.setCheckout(checkout);
		request.setAdults(adults);
		request.setChildren(children);
		request.setInfants(infants);
		request.setPets(pets);
		request.setPriceMin(priceMin);
		request.setPriceMax(priceMax);
		request.setMinBedrooms(minBedrooms);
		request.setMinBeds(minBeds);
		request.setAmenities(amenities);
		request.setGuestFavorite(guestFavorite);
		request.setIb(ib);
		request.setFlexibleDateSearchFilterType(flexibleDateSearchFilterType);
		request.setCurrency(currency);

		AirbnbSearchDTO respuesta = airbnbService.searchByPlaceId(request);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Buscar alojamientos por JSON", description = "Busca propiedades tipo Airbnb enviando los parametros en formato JSON.")
	@PostMapping(path = "/searchjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AirbnbSearchDTO> buscarPorPlaceIdJSON(@RequestBody AirbnbSearchRequestDTO request) {
		AirbnbSearchDTO respuesta = airbnbService.searchByPlaceId(request);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}
}