package co.edu.unbosque.travelx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import co.edu.unbosque.travelx.dto.AirbnbSearchDTO;
import co.edu.unbosque.travelx.dto.AirbnbSearchRequestDTO;

@Service
public class AirbnbService {

	@Value("${rapidapi.airbnb.host:airbnb19.p.rapidapi.com}")
	private String airbnbHost;

	@Value("${rapidapi.airbnb.key}")
	private String airbnbKey;

	private final RapidApiClient rapidApiClient;

	public AirbnbService(RapidApiClient rapidApiClient) {
		this.rapidApiClient = rapidApiClient;
	}

	public AirbnbSearchDTO searchByPlaceId(AirbnbSearchRequestDTO request) {
		String placeId = defaultString(request.getPlaceId(), "ChIJ7cv00DwsDogRAMDACa2m4K8");
		Integer adults = defaultInteger(request.getAdults(), 1);
		Boolean guestFavorite = defaultBoolean(request.getGuestFavorite(), false);
		Boolean ib = defaultBoolean(request.getIb(), false);
		String currency = defaultString(request.getCurrency(), "USD");

		StringBuilder url = new StringBuilder("https://" + airbnbHost + "/api/v2/searchPropertyByPlaceId");
		url.append("?placeId=").append(encode(placeId));
		url.append("&adults=").append(adults);
		url.append("&guestFavorite=").append(guestFavorite);
		url.append("&ib=").append(ib);
		url.append("&currency=").append(encode(currency));

		String json = rapidApiClient.doGet(url.toString(), airbnbHost, airbnbKey);

		AirbnbSearchDTO dto = new AirbnbSearchDTO();
		dto.setPlaceId(placeId);
		dto.setAdults(adults);
		dto.setGuestFavorite(guestFavorite);
		dto.setIb(ib);
		dto.setCurrency(currency);

		fillProviderStatus(dto, json);

		return dto;
	}
	
	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}

	private Integer defaultInteger(Integer value, Integer defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	private Boolean defaultBoolean(Boolean value, Boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	private String encode(String value) {
		return java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8);
	}

	private void fillProviderStatus(AirbnbSearchDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setSuccess(false);
			dto.setMessage("No se recibio respuesta del proveedor de Airbnb.");
			dto.setProviderResponse("");
			return;
		}

		try {
			JsonObject root = com.google.gson.JsonParser.parseString(json).getAsJsonObject();

			if (root.has("statusCode")) {
				dto.setStatusCode(root.get("statusCode").getAsInt());
				dto.setSuccess(false);

				String error = readString(root, "error");
				dto.setMessage(extractProviderMessage(error));
				dto.setProviderResponse(error);
				return;
			}

			if (root.has("status") && !root.get("status").isJsonNull() && !root.get("status").getAsBoolean()) {
				dto.setStatusCode(200);
				dto.setSuccess(false);

				if (root.has("message") && !root.get("message").isJsonNull()) {
					dto.setMessage(root.get("message").getAsString());
				} else {
					dto.setMessage("Error del proveedor de Airbnb.");
				}

				dto.setProviderResponse(json);
				return;
			}

			dto.setStatusCode(200);
			dto.setSuccess(true);
			dto.setMessage("Consulta realizada exitosamente.");
			dto.setProviderResponse(json);

		} catch (Exception e) {
			dto.setSuccess(false);
			dto.setMessage("No se pudo interpretar la respuesta del proveedor de Airbnb.");
			dto.setProviderResponse(json);
		}
	}

	private String extractProviderMessage(String error) {
		if (error == null || error.isBlank()) {
			return "Error desconocido del proveedor de Airbnb.";
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

	private String readString(com.google.gson.JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}

	private void appendIfPresent(StringBuilder url, String name, String value) {
		if (value != null && !value.isBlank()) {
			url.append("&").append(name).append("=").append(encode(value));
		}
	}
}