package co.edu.unbosque.travelx.service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.edu.unbosque.travelx.dto.MockAirbnbDTO;
import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;

@Service
public class MockAirbnbService {

	@Value("${mockapi.airbnb.url:https://6a0ff177d2a98570703608ed.mockapi.io/travelx/airbnb}")
	private String mockAirbnbUrl;

	private final SimpleHttpClientService simpleHttpClientService;
	private final Gson gson = new Gson();

	public MockAirbnbService(SimpleHttpClientService simpleHttpClientService) {
		this.simpleHttpClientService = simpleHttpClientService;
	}

	public TravelOptionDTO searchLodging(TravelSearchRequestDTO request) {
		String json = simpleHttpClientService.doGet(mockAirbnbUrl);

		TravelOptionDTO option = buildBaseOption(request);
		option.setProviderResponse(json);

		try {
			MockAirbnbDTO[] alojamientos = gson.fromJson(json, MockAirbnbDTO[].class);

			Optional<MockAirbnbDTO> match = Arrays.stream(alojamientos)
					.filter(item -> equalsNormalized(item.getCiudad(), request.getCiudadDestino()))
					.filter(item -> equalsNormalized(item.getPais(), request.getPaisDestino()))
					.filter(item -> request.getPiscina() == null || !request.getPiscina() || hasAmenity(item, "POOL"))
					.filter(item -> request.getJacuzzi() == null || !request.getJacuzzi() || hasAmenity(item, "JACUZZI"))
					.filter(item -> request.getPetFriendly() == null || !request.getPetFriendly()
							|| (item.getPets() != null && item.getPets() > 0))
					.filter(item -> request.getPrecioMinimo() == null || item.getPrecio_noche_usd() == null
							|| item.getPrecio_noche_usd() >= request.getPrecioMinimo())
					.filter(item -> request.getPrecioMaximo() == null || item.getPrecio_noche_usd() == null
							|| item.getPrecio_noche_usd() <= request.getPrecioMaximo())
					.findFirst();

			if (match.isEmpty()) {
				option.setAvailable(false);
				option.setProviderSuccess(false);
				option.setProviderStatusCode(404);
				option.setProviderMessage("No se encontro hospedaje para los filtros indicados.");
				option.setPrice(null);
				option.setPriceText("Precio no disponible");
				return option;
			}

			MockAirbnbDTO lodging = match.get();

			option.setAvailable(true);
			option.setProviderSuccess(true);
			option.setProviderStatusCode(200);
			option.setProviderMessage("Hospedaje encontrado.");
			option.setTitle(lodging.getNombre());
			option.setType("AIRBNB");
			option.setDescription(buildDescription(lodging));
			option.setPrice(lodging.getPrecio_noche_usd());
			option.setPriceText(lodging.getPrecio_noche_usd() + " " + defaultString(request.getMoneda(), "USD"));
			option.setHasPool(hasAmenity(lodging, "POOL"));
			option.setHasJacuzzi(hasAmenity(lodging, "JACUZZI"));
			option.setPetFriendly(lodging.getPets() != null && lodging.getPets() > 0);

			return option;

		} catch (Exception e) {
			option.setAvailable(false);
			option.setProviderSuccess(false);
			option.setProviderStatusCode(500);
			option.setProviderMessage("No se pudo interpretar la respuesta del mock de hospedajes.");
			option.setPrice(null);
			option.setPriceText("Precio no disponible");
			return option;
		}
	}

	private TravelOptionDTO buildBaseOption(TravelSearchRequestDTO request) {
		TravelOptionDTO option = new TravelOptionDTO();
		option.setProvider("MOCKAPI_AIRBNB");
		option.setType("AIRBNB");
		option.setTitle("Hospedaje");
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
		option.setTravelClass(request.getClaseViaje());
		return option;
	}

	private String buildDescription(MockAirbnbDTO lodging) {
		return defaultString(lodging.getTipo_alojamiento(), "Hospedaje")
				+ " - " + defaultInteger(lodging.getMinBedrooms(), 0) + " habitaciones"
				+ " - " + defaultInteger(lodging.getMinBeds(), 0) + " camas"
				+ " - " + defaultInteger(lodging.getBanos(), 0) + " banos"
				+ " - calificacion " + defaultDouble(lodging.getCalificacion(), 0.0);
	}

	private boolean hasAmenity(MockAirbnbDTO lodging, String amenity) {
		if (lodging.getAmenities() == null) {
			return false;
		}

		return lodging.getAmenities().toUpperCase().contains(amenity.toUpperCase());
	}

	private boolean equalsNormalized(String a, String b) {
		return normalize(a).equals(normalize(b));
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

	private Double defaultDouble(Double value, Double defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}
}