package co.edu.unbosque.travelx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.GoogleFlightsSearchDTO;
import co.edu.unbosque.travelx.dto.GoogleFlightsSearchRequestDTO;

/**
 * Servicio que gestiona la consulta de vuelos mediante la API de Google Flights
 * en RapidAPI, construyendo la URL de consulta, procesando la respuesta
 * y mapeando el resultado al DTO correspondiente.
 */
@Service
public class GoogleFlightsService {

	@Value("${rapidapi.googleflights.host:google-flights2.p.rapidapi.com}")
	private String googleFlightsHost;

	@Value("${rapidapi.googleflights.key}")
	private String googleFlightsKey;

	private final RapidApiClient rapidApiClient;

	public GoogleFlightsService(RapidApiClient rapidApiClient) {
		this.rapidApiClient = rapidApiClient;
	}

	/**
	 * Realiza una búsqueda de vuelos usando los parámetros del request,
	 * consultando la API externa y mapeando la respuesta al DTO.
	 *
	 * @param request objeto con los parámetros de búsqueda, incluyendo códigos IATA,
	 *                fechas, clase de viaje y configuración de pasajeros
	 * @return {@link GoogleFlightsSearchDTO} con los resultados y el estado de la consulta
	 */
	public GoogleFlightsSearchDTO searchFlights(GoogleFlightsSearchRequestDTO request) {
		String departureId = request.getDepartureId();
		String arrivalId = request.getArrivalId();
		String outboundDate = request.getOutboundDate();

		String travelClass = defaultString(request.getTravelClass(), "ECONOMY");
		String adults = defaultString(request.getAdults(), "1");
		String children = defaultString(request.getChildren(), "0");
		String infantOnLap = defaultString(request.getInfantOnLap(), "0");
		String infantInSeat = defaultString(request.getInfantInSeat(), "0");
		String showHidden = defaultString(request.getShowHidden(), "1");
		String currency = defaultString(request.getCurrency(), "USD");
		String languageCode = defaultString(request.getLanguageCode(), "en-US");
		String countryCode = defaultString(request.getCountryCode(), "US");
		String searchType = defaultString(request.getSearchType(), "best");

		StringBuilder url = new StringBuilder("https://" + googleFlightsHost + "/api/v1/searchFlights");
		url.append("?departure_id=").append(encode(departureId));
		url.append("&arrival_id=").append(encode(arrivalId));

		appendIfPresent(url, "outbound_date", outboundDate);
		appendIfPresent(url, "return_date", request.getReturnDate());

		url.append("&travel_class=").append(encode(travelClass));
		url.append("&adults=").append(encode(adults));
		url.append("&children=").append(encode(children));
		url.append("&infant_on_lap=").append(encode(infantOnLap));
		url.append("&infant_in_seat=").append(encode(infantInSeat));
		url.append("&show_hidden=").append(encode(showHidden));
		url.append("&currency=").append(encode(currency));
		url.append("&language_code=").append(encode(languageCode));
		url.append("&country_code=").append(encode(countryCode));
		url.append("&search_type=").append(encode(searchType));

		String json = rapidApiClient.doGet(url.toString(), googleFlightsHost, googleFlightsKey);

		GoogleFlightsSearchDTO dto = new GoogleFlightsSearchDTO();
		dto.setDepartureId(departureId);
		dto.setArrivalId(arrivalId);
		dto.setOutboundDate(outboundDate);
		dto.setReturnDate(request.getReturnDate());
		dto.setTravelClass(travelClass);
		dto.setAdults(adults);
		dto.setChildren(children);
		dto.setInfantOnLap(infantOnLap);
		dto.setInfantInSeat(infantInSeat);
		dto.setShowHidden(showHidden);
		dto.setCurrency(currency);
		dto.setLanguageCode(languageCode);
		dto.setCountryCode(countryCode);
		dto.setSearchType(searchType);

		fillProviderStatus(dto, json);

		return dto;
	}

	/**
	 * Interpreta la respuesta JSON del proveedor y establece el estado,
	 * mensaje y respuesta en el DTO según el resultado obtenido.
	 *
	 * @param dto  objeto donde se almacena el estado de la consulta
	 * @param json respuesta en formato JSON recibida del proveedor
	 */
	private void fillProviderStatus(GoogleFlightsSearchDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setSuccess(false);
			dto.setMessage("No se recibio respuesta del proveedor Google Flights.");
			dto.setProviderResponse("");
			return;
		}

		try {
			com.google.gson.JsonObject root = com.google.gson.JsonParser.parseString(json).getAsJsonObject();

			if (root.has("statusCode")) {
				dto.setStatusCode(root.get("statusCode").getAsInt());
				dto.setSuccess(false);

				String error = readString(root, "error");
				dto.setMessage(extractProviderMessage(error));
				dto.setProviderResponse(error);
				return;
			}

			dto.setStatusCode(200);
			dto.setSuccess(true);
			dto.setMessage("Consulta realizada exitosamente.");
			dto.setProviderResponse(json);

		} catch (Exception e) {
			dto.setSuccess(false);
			dto.setMessage("No se pudo interpretar la respuesta del proveedor Google Flights.");
			dto.setProviderResponse(json);
		}
	}

	/**
	 * Extrae el mensaje de error desde una cadena JSON de error del proveedor.
	 * Si no puede interpretarse como JSON, retorna la cadena original.
	 *
	 * @param error cadena con el error retornado por el proveedor
	 * @return mensaje de error legible extraído del JSON, o la cadena original
	 */
	private String extractProviderMessage(String error) {
		if (error == null || error.isBlank()) {
			return "Error desconocido del proveedor Google Flights.";
		}

		try {
			com.google.gson.JsonObject errorObject = com.google.gson.JsonParser.parseString(error).getAsJsonObject();

			if (errorObject.has("message") && !errorObject.get("message").isJsonNull()) {
				return errorObject.get("message").getAsString();
			}
		} catch (Exception e) {
			return error;
		}

		return error;
	}

	/**
	 * Lee un campo de texto de un objeto JSON de forma segura,
	 * retornando null si el campo no existe o es nulo.
	 *
	 * @param object    objeto JSON del que se desea leer el campo
	 * @param fieldName nombre del campo a leer
	 * @return valor del campo como texto, o null si no existe
	 */
	private String readString(com.google.gson.JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}

	/**
	 * Añade un parámetro a la URL solo si su valor no es nulo ni vacío.
	 *
	 * @param url   constructor de la URL donde se añade el parámetro
	 * @param name  nombre del parámetro en la URL
	 * @param value valor del parámetro a añadir
	 */
	private void appendIfPresent(StringBuilder url, String name, String value) {
		if (value != null && !value.isBlank()) {
			url.append("&").append(name).append("=").append(encode(value));
		}
	}

	/**
	 * Retorna el valor dado si no es nulo ni vacío, o el valor por defecto en caso contrario.
	 *
	 * @param value        valor a evaluar
	 * @param defaultValue valor por defecto a usar si el valor es nulo o vacío
	 * @return valor original o valor por defecto
	 */
	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}

	/**
	 * Codifica un valor de texto en formato URL usando UTF-8.
	 *
	 * @param value texto a codificar
	 * @return texto codificado para uso en URL
	 */
	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
}