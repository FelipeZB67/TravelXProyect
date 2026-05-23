package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
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
				+ "?city=" + encode(city)
				+ "&country=" + encode(country)
				+ "&format=json"
				+ "&limit=10"
				+ "&addressdetails=1"
				+ "&featureType=city"
				+ "&layer=address";

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
				dto.setDisplayName("No se encontro una ciudad con ese nombre en " + country + ".");
				return dto;
			}

			NominatimResolvedLocationDTO related = null;

			for (int i = 0; i < array.size(); i++) {
				JsonObject current = array.get(i).getAsJsonObject();

				if (!isCityLikeResult(current)) {
					continue;
				}

				NominatimResolvedLocationDTO candidate = buildDtoFromResult(current, query);

				if (related == null) {
					related = candidate;
				}

				if (countryMatches(candidate.getCountry(), country) && cityMatches(current, city)) {
					candidate.setFound(true);
					return candidate;
				}
			}

			if (related != null) {
				related.setFound(false);
				related.setDisplayName("No se encontro una ciudad exacta llamada " + city + " en " + country + ".");
				return related;
			}

			dto.setFound(false);
			dto.setDisplayName("No se encontro una ciudad valida con ese nombre en " + country + ".");
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

	private boolean isCityLikeResult(JsonObject result) {
		String resultClass = readString(result, "class");
		String resultType = readString(result, "type");
		String addresstype = readString(result, "addresstype");

		if ("place".equalsIgnoreCase(resultClass)) {
			return "city".equalsIgnoreCase(resultType)
					|| "town".equalsIgnoreCase(resultType)
					|| "village".equalsIgnoreCase(resultType)
					|| "hamlet".equalsIgnoreCase(resultType)
					|| "municipality".equalsIgnoreCase(resultType);
		}

		if ("boundary".equalsIgnoreCase(resultClass)) {
			return "administrative".equalsIgnoreCase(resultType)
					&& ("city".equalsIgnoreCase(addresstype)
					|| "town".equalsIgnoreCase(addresstype)
					|| "village".equalsIgnoreCase(addresstype)
					|| "municipality".equalsIgnoreCase(addresstype));
		}

		return false;
	}

	private boolean cityMatches(JsonObject result, String requestedCity) {
		if (result.has("address") && result.get("address").isJsonObject()) {
			JsonObject address = result.getAsJsonObject("address");

			if (matchesAnyAddressField(address, requestedCity)) {
				return true;
			}
		}

		String displayName = readString(result, "display_name");
		if (displayName != null && displayName.contains(",")) {
			String firstPart = displayName.split(",")[0];
			return normalize(firstPart).equals(normalize(requestedCity));
		}

		return false;
	}

	private boolean matchesAnyAddressField(JsonObject address, String requestedCity) {
		String[] fields = { "city", "town", "village", "municipality", "hamlet" };

		for (String field : fields) {
			String value = readString(address, field);
			if (normalize(value).equals(normalize(requestedCity))) {
				return true;
			}
		}

		return false;
	}

	private NominatimResolvedLocationDTO buildDtoFromResult(JsonObject result, String query) {
		NominatimResolvedLocationDTO dto = new NominatimResolvedLocationDTO();
		dto.setQuery(query);
		dto.setDisplayName(readString(result, "display_name"));
		dto.setLatitude(readString(result, "lat"));
		dto.setLongitude(readString(result, "lon"));
		dto.setFound(false);

		if (result.has("address") && result.get("address").isJsonObject()) {
			JsonObject address = result.getAsJsonObject("address");
			dto.setCountry(readString(address, "country"));
			dto.setCountryCode(readString(address, "country_code"));
		}

		return dto;
	}

	private boolean countryMatches(String foundCountry, String requestedCountry) {
		return normalize(foundCountry).equals(normalize(requestedCountry));
	}

	private String normalize(String value) {
		if (value == null) {
			return "";
		}

		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim().toLowerCase();
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