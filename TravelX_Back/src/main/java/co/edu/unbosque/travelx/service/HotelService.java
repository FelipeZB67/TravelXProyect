package co.edu.unbosque.travelx.service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import co.edu.unbosque.travelx.dto.HotelDetailsDTO;
import co.edu.unbosque.travelx.dto.HotelDetailsRequestDTO;

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
	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}

	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
	
	private void fillProviderStatus(HotelDetailsDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setSuccess(false);
			dto.setMessage("No se recibio respuesta del proveedor de hoteles.");
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
			dto.setMessage("No se pudo interpretar la respuesta del proveedor de hoteles.");
			dto.setProviderResponse(json);
		}
	}

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

	private String readString(com.google.gson.JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}
}