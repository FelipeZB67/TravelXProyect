package co.edu.unbosque.travelx.service;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.travelx.dto.MockFlightDTO;
import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;

@Service
public class MockFlightService {

	@Value("${mockapi.flights.url:https://6a123de478d0434e0d5d24c9.mockapi.io/travelx/vuelos}")
	private String mockFlightsUrl;

	private final SimpleHttpClientService simpleHttpClientService;
	private final Gson gson = new Gson();

	public MockFlightService(SimpleHttpClientService simpleHttpClientService) {
		this.simpleHttpClientService = simpleHttpClientService;
	}

	public TravelOptionDTO searchFlight(TravelSearchRequestDTO request) {
		String json = simpleHttpClientService.doGet(mockFlightsUrl);

		TravelOptionDTO option = buildBaseOption(request);
		option.setProviderResponse(json);

		try {
			Type listType = new TypeToken<List<MockFlightDTO>>() {
			}.getType();

			List<MockFlightDTO> flights = gson.fromJson(json, listType);
			MockFlightDTO flight = findFlight(flights, request);

			if (flight == null) {
				option.setAvailable(false);
				option.setProviderSuccess(false);
				option.setProviderStatusCode(404);
				option.setProviderMessage("No se encontro vuelo en MockAPI para el origen y destino indicados.");
				option.setPrice(null);
				option.setPriceText("Precio no disponible");
				return option;
			}

			option.setAvailable(true);
			option.setProviderSuccess(true);
			option.setProviderStatusCode(200);
			option.setProviderMessage("Vuelo encontrado en MockAPI.");
			option.setProvider(defaultString(flight.getProvider(), "MOCKAPI_FLIGHTS"));
			option.setType(defaultString(flight.getType(), "FLIGHT"));
			option.setTitle(defaultString(flight.getTitle(), "Vuelo encontrado"));
			option.setDescription(flight.getDescription());
			option.setOriginCity(flight.getOriginCity());
			option.setOriginCountry(flight.getOriginCountry());
			option.setDestinationCity(flight.getDestinationCity());
			option.setDestinationCountry(flight.getDestinationCountry());
			option.setDepartureDate(flight.getDepartureDate());
			option.setReturnDate(flight.getReturnDate());
			option.setCurrency(defaultString(flight.getCurrency(), defaultString(request.getMoneda(), "USD")));
			option.setPrice(flight.getPrice());
			option.setPriceText(defaultString(flight.getPriceText(), flight.getPrice() + " " + option.getCurrency()));
			option.setAdults(defaultInteger(request.getAdultos(), defaultInteger(flight.getAdults(), 1)));
			option.setChildren(defaultInteger(request.getNinos(), defaultInteger(flight.getChildren(), 0)));
			option.setPets(defaultInteger(request.getMascotas(), defaultInteger(flight.getPets(), 0)));
			option.setTravelClass(defaultString(request.getClaseViaje(), defaultString(flight.getTravelClass(), "ECONOMY")));
			option.setProviderResponse(gson.toJson(flight));

			return option;

		} catch (Exception e) {
			option.setAvailable(false);
			option.setProviderSuccess(false);
			option.setProviderStatusCode(500);
			option.setProviderMessage("No se pudo interpretar la respuesta del mock de vuelos.");
			option.setPrice(null);
			option.setPriceText("Precio no disponible");
			return option;
		}
	}

	private TravelOptionDTO buildBaseOption(TravelSearchRequestDTO request) {
		TravelOptionDTO option = new TravelOptionDTO();
		option.setProvider("MOCKAPI_FLIGHTS");
		option.setType("FLIGHT");
		option.setTitle("Vuelo MockAPI");
		option.setOriginCity(request.getCiudadOrigen());
		option.setOriginCountry(request.getPaisOrigen());
		option.setDestinationCity(request.getCiudadDestino());
		option.setDestinationCountry(request.getPaisDestino());
		option.setDepartureDate(request.getFechaSalida());
		option.setReturnDate(request.getFechaRegreso());
		option.setCurrency(defaultString(request.getMoneda(), "USD"));
		option.setAdults(defaultInteger(request.getAdultos(), 1));
		option.setChildren(defaultInteger(request.getNinos(), 0));
		option.setPets(defaultInteger(request.getMascotas(), 0));
		option.setTravelClass(defaultString(request.getClaseViaje(), "ECONOMY"));
		return option;
	}

	private MockFlightDTO findFlight(List<MockFlightDTO> flights, TravelSearchRequestDTO request) {
		if (flights == null) {
			return null;
		}

		String originCity = normalize(request.getCiudadOrigen());
		String originCountry = normalize(request.getPaisOrigen());
		String destinationCity = normalize(request.getCiudadDestino());
		String destinationCountry = normalize(request.getPaisDestino());

		return flights.stream()
				.filter(flight -> normalize(flight.getOriginCity()).equals(originCity))
				.filter(flight -> normalize(flight.getOriginCountry()).equals(originCountry))
				.filter(flight -> normalize(flight.getDestinationCity()).equals(destinationCity))
				.filter(flight -> normalize(flight.getDestinationCountry()).equals(destinationCountry))
				.findFirst()
				.orElse(null);
	}

	private String normalize(String value) {
		if (value == null) {
			return "";
		}

		return Normalizer.normalize(value, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.trim()
				.toLowerCase();
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
}