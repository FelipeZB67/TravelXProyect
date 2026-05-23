package co.edu.unbosque.travelx.service;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.travelx.dto.TerrestrialRouteDTO;
import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;

@Service
public class TerrestrialTransportService {

	private static final String ROUTES_URL = "https://6a0ff177d2a98570703608ed.mockapi.io/travelx/rutas";

	private final SimpleHttpClientService simpleHttpClientService;

	public TerrestrialTransportService(SimpleHttpClientService simpleHttpClientService) {
		this.simpleHttpClientService = simpleHttpClientService;
	}

	public TravelOptionDTO searchRoute(TravelSearchRequestDTO request) {
		TravelOptionDTO option = new TravelOptionDTO();
		option.setProvider("MOCKAPI_TERRESTRIAL");
		option.setType("BUS");
		option.setTitle("Transporte terrestre");
		option.setOriginCity(request.getCiudadOrigen());
		option.setOriginCountry(request.getPaisOrigen());
		option.setDestinationCity(request.getCiudadDestino());
		option.setDestinationCountry(request.getPaisDestino());
		option.setCurrency("USD");

		String json = simpleHttpClientService.doGet(ROUTES_URL);

		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<List<TerrestrialRouteDTO>>() {
			}.getType();

			List<TerrestrialRouteDTO> routes = gson.fromJson(json, listType);
			TerrestrialRouteDTO route = findRoute(routes, request);

			if (route == null) {
				option.setAvailable(false);
				option.setProviderSuccess(false);
				option.setProviderStatusCode(404);
				option.setProviderMessage("No se encontro ruta terrestre para el origen y destino indicados.");
				option.setPrice(null);
				option.setPriceText("Precio no disponible");
				option.setProviderResponse(json);
				return option;
			}

			option.setAvailable(true);
			option.setProviderSuccess(true);
			option.setProviderStatusCode(200);
			option.setProviderMessage("Ruta terrestre encontrada.");
			option.setDescription(route.getOperador() + " - " + route.getTipo_bus() + " - " + route.getFrecuencia());
			option.setPrice(route.getPrecio_usd());
			option.setPriceText(route.getPrecio_usd() + " USD");
			option.setProviderResponse(gson.toJson(route));

			return option;

		} catch (Exception e) {
			option.setAvailable(false);
			option.setProviderSuccess(false);
			option.setProviderStatusCode(500);
			option.setProviderMessage("No se pudo interpretar la respuesta de transporte terrestre.");
			option.setPrice(null);
			option.setPriceText("Precio no disponible");
			option.setProviderResponse(json);
			return option;
		}
	}

	private TerrestrialRouteDTO findRoute(List<TerrestrialRouteDTO> routes, TravelSearchRequestDTO request) {
		if (routes == null) {
			return null;
		}

		String origin = normalize(request.getCiudadOrigen());
		String destination = normalize(request.getCiudadDestino());
		String originCountry = normalize(request.getPaisOrigen());
		String destinationCountry = normalize(request.getPaisDestino());

		for (TerrestrialRouteDTO route : routes) {
			boolean sameOrigin = normalize(route.getOrigen()).equals(origin);
			boolean sameDestination = normalize(route.getDestino()).equals(destination);
			boolean sameOriginCountry = normalize(route.getPais_origen()).equals(originCountry);
			boolean sameDestinationCountry = normalize(route.getPais_destino()).equals(destinationCountry);

			if (sameOrigin && sameDestination && sameOriginCountry && sameDestinationCountry) {
				return route;
			}
		}

		return null;
	}

	private String normalize(String value) {
		if (value == null) {
			return "";
		}

		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim().toLowerCase();
	}
}