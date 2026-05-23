package co.edu.unbosque.travelx.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
		Integer adults = request.getAdults() == null ? 1 : request.getAdults();
		Integer children = request.getChildren() == null ? 0 : request.getChildren();
		Integer infants = request.getInfants() == null ? 0 : request.getInfants();
		Integer pets = request.getPets() == null ? 0 : request.getPets();
		Integer priceMin = request.getPriceMin() == null ? 0 : request.getPriceMin();
		Integer priceMax = request.getPriceMax() == null ? 0 : request.getPriceMax();
		Integer minBedrooms = request.getMinBedrooms() == null ? 0 : request.getMinBedrooms();
		Integer minBeds = request.getMinBeds() == null ? 0 : request.getMinBeds();
		Boolean guestFavorite = request.getGuestFavorite() == null ? false : request.getGuestFavorite();
		Boolean ib = request.getIb() == null ? false : request.getIb();
		String currency = defaultString(request.getCurrency(), "USD");

		StringBuilder url = new StringBuilder("https://" + airbnbHost + "/api/v2/searchPropertyByPlaceId");
		url.append("?placeId=").append(encode(request.getPlaceId()));
		url.append("&adults=").append(adults);
		url.append("&children=").append(children);
		url.append("&infants=").append(infants);
		url.append("&pets=").append(pets);
		url.append("&priceMin=").append(priceMin);
		url.append("&priceMax=").append(priceMax);
		url.append("&minBedrooms=").append(minBedrooms);
		url.append("&minBeds=").append(minBeds);
		url.append("&guestFavorite=").append(guestFavorite);
		url.append("&ib=").append(ib);
		url.append("&currency=").append(encode(currency));

		appendIfPresent(url, "nextPageCursor", request.getNextPageCursor());
		appendIfPresent(url, "checkin", request.getCheckin());
		appendIfPresent(url, "checkout", request.getCheckout());
		appendIfPresent(url, "amenities", request.getAmenities());
		appendIfPresent(url, "flexibleDateSearchFilterType", request.getFlexibleDateSearchFilterType());

		String json = rapidApiClient.doGet(url.toString(), airbnbHost, airbnbKey);

		AirbnbSearchDTO dto = new AirbnbSearchDTO();
		dto.setPlaceId(request.getPlaceId());
		dto.setNextPageCursor(request.getNextPageCursor());
		dto.setCheckin(request.getCheckin());
		dto.setCheckout(request.getCheckout());
		dto.setAdults(adults);
		dto.setChildren(children);
		dto.setInfants(infants);
		dto.setPets(pets);
		dto.setPriceMin(priceMin);
		dto.setPriceMax(priceMax);
		dto.setMinBedrooms(minBedrooms);
		dto.setMinBeds(minBeds);
		dto.setAmenities(request.getAmenities());
		dto.setGuestFavorite(guestFavorite);
		dto.setIb(ib);
		dto.setFlexibleDateSearchFilterType(request.getFlexibleDateSearchFilterType());
		dto.setCurrency(currency);

		fillProviderStatus(dto, json);

		return dto;
	}

	private void fillProviderStatus(AirbnbSearchDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setSuccess(false);
			dto.setMessage("No se recibio respuesta del proveedor de Airbnb.");
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

	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
}