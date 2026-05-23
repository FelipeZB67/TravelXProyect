package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.NominatimResolvedLocationDTO;

@Service
public class NominatimService {

	private static final String BASE_URL = "https://nominatim.openstreetmap.org/search";

	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.build();

	public NominatimResolvedLocationDTO searchCity(String city, String country) {
		String query = city + ", " + country;

		NominatimResolvedLocationDTO dto = new NominatimResolvedLocationDTO();
		dto.setQuery(query);

		String url = BASE_URL
				+ "?q=" + encode(query)
				+ "&format=json"
				+ "&limit=1"
				+ "&addressdetails=1";

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(20))
				.header("User-Agent", "TravelX/1.0 contacto@travelx.com")
				.header("Accept", "application/json")
				.build();

		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				dto.setFound(false);
				dto.setDisplayName("Error HTTP " + response.statusCode());
				return dto;
			}

			JsonArray array = JsonParser.parseString(response.body()).getAsJsonArray();

			if (array.isEmpty()) {
				dto.setFound(false);
				dto.setDisplayName("No se encontro ubicacion.");
				return dto;
			}

			JsonObject first = array.get(0).getAsJsonObject();

			dto.setFound(true);
			dto.setDisplayName(readString(first, "display_name"));
			dto.setLatitude(readString(first, "lat"));
			dto.setLongitude(readString(first, "lon"));

			if (first.has("address") && first.get("address").isJsonObject()) {
				JsonObject address = first.getAsJsonObject("address");
				dto.setCountry(readString(address, "country"));
				dto.setCountryCode(readString(address, "country_code"));
			}

			return dto;

		} catch (IOException e) {
			dto.setFound(false);
			dto.setDisplayName("Error de conexion: " + e.getMessage());
			return dto;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			dto.setFound(false);
			dto.setDisplayName("Peticion interrumpida: " + e.getMessage());
			return dto;
		} catch (Exception e) {
			dto.setFound(false);
			dto.setDisplayName("Error al interpretar respuesta: " + e.getMessage());
			return dto;
		}
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
}