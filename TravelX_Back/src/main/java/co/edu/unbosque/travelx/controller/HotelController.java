package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.HotelDetailsDTO;
import co.edu.unbosque.travelx.dto.HotelDetailsRequestDTO;
import co.edu.unbosque.travelx.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la consulta de detalles de hoteles desde RapidAPI,
 * delegando la lógica al servicio {@link HotelService}.
 */
@RestController
@RequestMapping("/hotel")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
@Tag(name = "Consulta de Hoteles", description = "Endpoints para consultar hoteles desde RapidAPI")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	public HotelController() {
	}

	/**
	 * Consulta el detalle de un hotel usando su ID y filtros opcionales
	 * enviados como parámetros de consulta en la URL.
	 *
	 * @param id       identificador del hotel a consultar
	 * @param checkIn  fecha de entrada
	 * @param checkOut fecha de salida
	 * @param adults1  número de adultos
	 * @param children1 edades de los niños separadas por comas
	 * @param currency moneda en la que se muestran los precios
	 * @param locale   idioma y región para la respuesta
	 * @return {@link ResponseEntity} con el detalle del hotel y código 202 si es exitoso,
	 *         o 400 si la respuesta es nula o indica fallo
	 */
	@Operation(summary = "Consultar detalle de hotel", description = "Consulta el detalle de un hotel usando el id retornado por la API de hoteles.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Consulta realizada exitosamente"),
			@ApiResponse(responseCode = "400", description = "Parametros invalidos o error al consultar la API externa")
	})
	@GetMapping("/details")
	public ResponseEntity<HotelDetailsDTO> consultarDetalleHotel(
			@RequestParam Long id,
			@RequestParam(required = false) String checkIn,
			@RequestParam(required = false) String checkOut,
			@RequestParam(required = false, defaultValue = "1") Integer adults1,
			@RequestParam(required = false) String children1,
			@RequestParam(required = false, defaultValue = "USD") String currency,
			@RequestParam(required = false, defaultValue = "en_US") String locale) {

		HotelDetailsRequestDTO request = new HotelDetailsRequestDTO();
		request.setId(id);
		request.setCheckIn(checkIn);
		request.setCheckOut(checkOut);
		request.setAdults1(adults1);
		request.setChildren1(children1);
		request.setCurrency(currency);
		request.setLocale(locale);

		HotelDetailsDTO respuesta = hotelService.getDetails(request);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}

	/**
	 * Consulta el detalle de un hotel recibiendo todos los parámetros
	 * encapsulados en un objeto JSON en el cuerpo de la petición.
	 *
	 * @param request objeto con el ID del hotel y los filtros de consulta
	 * @return {@link ResponseEntity} con el detalle del hotel y código 202 si es exitoso,
	 *         o 400 si la respuesta es nula o indica fallo
	 */
	@Operation(summary = "Consultar detalle de hotel por JSON", description = "Consulta el detalle de un hotel enviando los parametros en formato JSON.")
	@PostMapping(path = "/detailsjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDetailsDTO> consultarDetalleHotelJSON(@RequestBody HotelDetailsRequestDTO request) {
		HotelDetailsDTO respuesta = hotelService.getDetails(request);

		if (respuesta == null || Boolean.FALSE.equals(respuesta.getSuccess())) {
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(respuesta, HttpStatus.ACCEPTED);
	}
}