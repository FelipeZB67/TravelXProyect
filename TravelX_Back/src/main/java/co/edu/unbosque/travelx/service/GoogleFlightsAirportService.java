package co.edu.unbosque.travelx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.AirportCodeDTO;

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

	private String readString(JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	private String removeAccents(String value) {
		if (value == null) {
			return "";
		}

		String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim();
	}
}