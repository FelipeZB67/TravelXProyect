package co.edu.unbosque.travelx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.AirportCodeDTO;

/**
 * Servicio que consulta aeropuertos y sus códigos IATA mediante la API
 * de Google Flights en RapidAPI, procesando y mapeando la respuesta al DTO correspondiente.
 */
@Service
public class GoogleFlightsAirportService {

	@Value("${rapidapi.googleflights.host:google-flights2.p.rapidapi.com}")
	private String googleFlightsHost;

	@Value("${rapidapi.googleflights.key}")
	private String googleFlightsKey;

	private final RapidApiClient rapidApiClient;

	public GoogleFlightsAirportService(RapidApiClient rapidApiClient) {
		this.rapidApiClient = rapidApiClient;
	}

	/**
	 * Busca un aeropuerto según el nombre o ciudad y el código de país,
	 * retornando su código IATA y nombre si es encontrado.
	 *
	 * @param query       nombre o ciudad del aeropuerto a buscar
	 * @param countryCode código del país donde se ubica el aeropuerto
	 * @return {@link AirportCodeDTO} con el código IATA y el estado de la consulta
	 */
	public AirportCodeDTO searchAirport(String query, String countryCode) {
		AirportCodeDTO dto = new AirportCodeDTO();
		dto.setQuery(removeAccents(query));
		dto.setCountryCode(countryCode);

		String normalizedQuery = removeAccents(query);

		String url = "https://" + googleFlightsHost + "/api/v1/searchAirport" + "?query=" + encode(normalizedQuery);

		String json = rapidApiClient.doGet(url, googleFlightsHost, googleFlightsKey);
		dto.setProviderResponse(json);

		try {
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();

			if (root.has("statusCode")) {
				dto.setFound(false);
				dto.setMessage(extractProviderError(root));
				return dto;
			}

			JsonArray data = null;

			if (root.has("data") && root.get("data").isJsonArray()) {
				data = root.getAsJsonArray("data");
			}

			if (data == null || data.isEmpty()) {
				dto.setFound(false);
				dto.setMessage("No se encontro aeropuerto para la busqueda.");
				return dto;
			}

			JsonObject first = data.get(0).getAsJsonObject();

			String iataCode = null;
			String airportName = null;

			if (first.has("list") && first.get("list").isJsonArray() && !first.getAsJsonArray("list").isEmpty()) {
				JsonObject firstAirport = first.getAsJsonArray("list").get(0).getAsJsonObject();
				iataCode = readString(firstAirport, "id");
				airportName = readString(firstAirport, "title");
			} else if ("airport".equalsIgnoreCase(readString(first, "type"))) {
				iataCode = readString(first, "id");
				airportName = readString(first, "title");
			}

			dto.setIataCode(iataCode);
			dto.setName(airportName);
			dto.setFound(iataCode != null && !iataCode.isBlank());
			dto.setMessage(dto.getFound() ? "Aeropuerto encontrado." : "No se encontro codigo IATA.");

			return dto;

		} catch (Exception e) {
			dto.setFound(false);
			dto.setMessage("No se pudo interpretar la respuesta de aeropuertos.");
			return dto;
		}
	}

	/**
	 * Extrae el mensaje de error desde el objeto JSON de respuesta del proveedor.
	 *
	 * @param root objeto JSON raíz de la respuesta del proveedor
	 * @return mensaje de error legible, o un mensaje genérico si no puede extraerse
	 */
	private String extractProviderError(JsonObject root) {
		String error = readString(root, "error");

		if (error == null || error.isBlank()) {
			return "Error desconocido del proveedor Google Flights.";
		}

		try {
			JsonObject errorObject = JsonParser.parseString(error).getAsJsonObject();
			String message = readString(errorObject, "message");

			if (message != null && !message.isBlank()) {
				return message;
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
	private String readString(JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
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

	/**
	 * Elimina tildes y diacríticos de un texto normalizando su forma Unicode.
	 *
	 * @param value texto del que se desean eliminar los acentos
	 * @return texto sin acentos ni caracteres diacríticos, o cadena vacía si es nulo
	 */
	private String removeAccents(String value) {
		if (value == null) {
			return "";
		}

		String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim();
	}
}