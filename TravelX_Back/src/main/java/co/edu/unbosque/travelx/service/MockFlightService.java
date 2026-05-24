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

/**
 * Servicio que consulta vuelos desde una MockAPI, filtrando por origen y destino
 * para retornar la opción de vuelo más adecuada según el request.
 */
@Service
public class MockFlightService {

	@Value("${mockapi.flights.url:https://6a123de478d0434e0d5d24c9.mockapi.io/travelx/vuelos}")
	private String mockFlightsUrl;

	private final SimpleHttpClientService simpleHttpClientService;
	private final Gson gson = new Gson();

	public MockFlightService(SimpleHttpClientService simpleHttpClientService) {
		this.simpleHttpClientService = simpleHttpClientService;
	}

	/**
	 * Busca un vuelo disponible en la MockAPI según los parámetros del request
	 * y mapea el resultado a un {@link TravelOptionDTO}.
	 *
	 * @param request objeto con los parámetros de búsqueda, incluyendo origen,
	 *                destino, fechas y configuración de pasajeros
	 * @return {@link TravelOptionDTO} con el vuelo encontrado y el estado de la consulta
	 */
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

	/**
	 * Construye un {@link TravelOptionDTO} base con los datos comunes del request
	 * antes de aplicar los resultados de la búsqueda.
	 *
	 * @param request objeto con los parámetros de búsqueda
	 * @return {@link TravelOptionDTO} inicializado con los datos del request
	 */
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

	/**
	 * Busca el primer vuelo de la lista que coincida con el origen y destino
	 * del request, normalizando los valores para la comparación.
	 *
	 * @param flights lista de vuelos obtenida desde la MockAPI
	 * @param request objeto con los parámetros de búsqueda
	 * @return primer {@link MockFlightDTO} que coincida, o {@code null} si no hay coincidencias
	 */
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

		return Normalizer.normalize(value, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.trim()
				.toLowerCase();
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

	/**
	 * Retorna el valor dado si no es nulo, o el valor por defecto en caso contrario.
	 *
	 * @param value        valor a evaluar
	 * @param defaultValue valor por defecto a usar si el valor es nulo
	 * @return valor original o valor por defecto
	 */
	private Integer defaultInteger(Integer value, Integer defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}
}