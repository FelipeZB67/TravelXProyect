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

/**
 * Servicio que resuelve ciudades a coordenadas geográficas y datos normalizados
 * mediante la API pública de OpenStreetMap Nominatim.
 */
@Service
public class NominatimService {

	private static final String BASE_URL = "https://nominatim.openstreetmap.org/search";

	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(10))
			.build();

	/**
	 * Busca una ciudad por nombre y país, retornando sus coordenadas
	 * y datos de ubicación normalizados desde Nominatim.
	 *
	 * @param city    nombre de la ciudad a buscar
	 * @param country país donde se ubica la ciudad
	 * @return {@link NominatimResolvedLocationDTO} con la ubicación resuelta,
	 *         o con {@code found} en {@code false} si no se encuentra o hay un error
	 */
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

	/**
	 * Verifica si un resultado de Nominatim corresponde a un tipo de asentamiento
	 * urbano válido como ciudad, pueblo, aldea o municipio.
	 *
	 * @param result objeto JSON con los datos del resultado a evaluar
	 * @return {@code true} si el resultado representa un asentamiento urbano válido
	 */
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

	/**
	 * Verifica si el resultado de Nominatim corresponde a la ciudad solicitada,
	 * comparando los campos de dirección y el nombre de visualización.
	 *
	 * @param result        objeto JSON con los datos del resultado
	 * @param requestedCity nombre de la ciudad solicitada
	 * @return {@code true} si el resultado coincide con la ciudad buscada
	 */
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

	/**
	 * Compara la ciudad solicitada contra los campos de dirección del resultado
	 * de Nominatim, incluyendo city, town, village, municipality y hamlet.
	 *
	 * @param address       objeto JSON con los campos de dirección del resultado
	 * @param requestedCity nombre de la ciudad a comparar
	 * @return {@code true} si algún campo de dirección coincide con la ciudad solicitada
	 */
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

	/**
	 * Construye un {@link NominatimResolvedLocationDTO} a partir de un resultado
	 * JSON de Nominatim, extrayendo coordenadas, nombre y datos del país.
	 *
	 * @param result objeto JSON con los datos del resultado de Nominatim
	 * @param query  consulta original realizada
	 * @return {@link NominatimResolvedLocationDTO} con los datos extraídos del resultado
	 */
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

	/**
	 * Compara el país encontrado con el país solicitado normalizando ambos valores.
	 *
	 * @param foundCountry     nombre del país obtenido en el resultado
	 * @param requestedCountry nombre del país solicitado en la búsqueda
	 * @return {@code true} si ambos países son equivalentes tras la normalización
	 */
	private boolean countryMatches(String foundCountry, String requestedCountry) {
		return normalize(foundCountry).equals(normalize(requestedCountry));
	}

	/**
	 * Normaliza un texto eliminando tildes y diacríticos, convirtiéndolo
	 * a minúsculas y eliminando espacios extremos.
	 *
	 * @param value texto a normalizar
	 * @return texto normalizado, o cadena vacía si es nulo
	 */
	private String normalize(String value) {
		if (value == null) {
			return "";
		}

		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim().toLowerCase();
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
}