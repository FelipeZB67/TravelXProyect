package co.edu.unbosque.travelx.service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.HotelDetailsDTO;
import co.edu.unbosque.travelx.dto.HotelDetailsRequestDTO;

/**
 * Servicio que gestiona la consulta de detalles de hoteles mediante la API
 * de Hotels en RapidAPI, construyendo la URL de consulta, procesando la respuesta
 * y mapeando el resultado al DTO correspondiente.
 */
@Service
public class HotelService {

	@Value("${rapidapi.hotels.host:hotels4.p.rapidapi.com}")
	private String hotelsHost;

	@Value("${rapidapi.hotels.key}")
	private String hotelsKey;

	private final RapidApiClient rapidApiClient;

	public HotelService(RapidApiClient rapidApiClient) {
		this.rapidApiClient = rapidApiClient;
	}

	/**
	 * Consulta el detalle de un hotel usando su ID y los filtros del request,
	 * consultando la API externa y mapeando la respuesta al DTO.
	 *
	 * @param request objeto con el ID del hotel, fechas de estancia y configuración de huéspedes
	 * @return {@link HotelDetailsDTO} con los detalles del hotel y el estado de la consulta
	 */
	public HotelDetailsDTO getDetails(HotelDetailsRequestDTO request) {
		String checkIn = defaultString(request.getCheckIn(), "2020-01-08");
		String checkOut = defaultString(request.getCheckOut(), "2020-01-15");
		Integer adults1 = request.getAdults1() == null ? 1 : request.getAdults1();
		String currency = defaultString(request.getCurrency(), "USD");
		String locale = defaultString(request.getLocale(), "en_US");

		StringBuilder url = new StringBuilder("https://" + hotelsHost + "/properties/get-details");
		url.append("?id=").append(request.getId());
		url.append("&checkIn=").append(encode(checkIn));
		url.append("&checkOut=").append(encode(checkOut));
		url.append("&adults1=").append(adults1);
		url.append("&currency=").append(encode(currency));
		url.append("&locale=").append(encode(locale));

		if (request.getChildren1() != null && !request.getChildren1().isBlank()) {
			url.append("&children1=").append(encode(request.getChildren1()));
		}

		String json = rapidApiClient.doGet(url.toString(), hotelsHost, hotelsKey);

		HotelDetailsDTO dto = new HotelDetailsDTO();
		dto.setId(request.getId());
		dto.setCheckIn(checkIn);
		dto.setCheckOut(checkOut);
		dto.setAdults(adults1);
		dto.setChildren(request.getChildren1());
		dto.setCurrency(currency);
		dto.setLocale(locale);

		fillProviderStatus(dto, json);

		return dto;

		
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

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
	
	/**
	 * Interpreta la respuesta JSON del proveedor y establece el estado,
	 * mensaje y respuesta en el DTO según el resultado obtenido.
	 *
	 * @param dto  objeto donde se almacena el estado de la consulta
	 * @param json respuesta en formato JSON recibida del proveedor
	 */
	private void fillProviderStatus(HotelDetailsDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setSuccess(false);
			dto.setMessage("No se recibio respuesta del proveedor de hoteles.");
			dto.setProviderResponse("");
			return;
		}

		try {
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();

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
			dto.setMessage("No se pudo interpretar la respuesta del proveedor de hoteles.");
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
			return "Error desconocido del proveedor de hoteles.";
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
}