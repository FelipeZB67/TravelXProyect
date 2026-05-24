package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.AirbnbSearchDTO;
import co.edu.unbosque.travelx.dto.AirbnbSearchRequestDTO;
import co.edu.unbosque.travelx.dto.AirportCodeDTO;
import co.edu.unbosque.travelx.dto.DestinationCodeDTO;
import co.edu.unbosque.travelx.dto.GoogleFlightsSearchDTO;
import co.edu.unbosque.travelx.dto.GoogleFlightsSearchRequestDTO;
import co.edu.unbosque.travelx.dto.NominatimResolvedLocationDTO;
import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.dto.TravelSearchRequestDTO;
import co.edu.unbosque.travelx.dto.TravelSearchResponseDTO;

/**
 * Servicio unificado de búsqueda de viajes que orquesta la consulta a múltiples
 * proveedores externos (vuelos, Airbnb, transporte terrestre y hoteles),
 * resolviendo ubicaciones y consolidando los resultados en una respuesta única.
 */
@Service
public class TravelSearchService {

	private final GoogleFlightsService googleFlightsService;
	private final AirbnbService airbnbService;
	private final DestinationResolverService destinationResolverService;
	private final PriceExtractorService priceExtractorService;
	private final TerrestrialTransportService terrestrialTransportService;
	private final NominatimService nominatimService;
	private final GoogleFlightsAirportService googleFlightsAirportService;
	private final MockAirbnbService mockAirbnbService;
	private final MockFlightService mockFlightService;

	public TravelSearchService(GoogleFlightsService googleFlightsService, AirbnbService airbnbService,
			DestinationResolverService destinationResolverService, PriceExtractorService priceExtractorService,
			TerrestrialTransportService terrestrialTransportService, NominatimService nominatimService,
			GoogleFlightsAirportService googleFlightsAirportService, MockAirbnbService mockAirbnbService,
			MockFlightService mockFlightService) {

		this.googleFlightsService = googleFlightsService;
		this.airbnbService = airbnbService;
		this.destinationResolverService = destinationResolverService;
		this.priceExtractorService = priceExtractorService;
		this.terrestrialTransportService = terrestrialTransportService;
		this.nominatimService = nominatimService;
		this.googleFlightsAirportService = googleFlightsAirportService;
		this.mockAirbnbService = mockAirbnbService;
		this.mockFlightService = mockFlightService;
	}

	/**
	 * Ejecuta una búsqueda unificada de opciones de viaje según los tipos de servicio
	 * indicados en el request, validando las ubicaciones de origen y destino
	 * antes de consultar cada proveedor.
	 *
	 * @param request objeto con los parámetros de búsqueda y los tipos de servicio a incluir
	 * @return {@link TravelSearchResponseDTO} con las opciones encontradas y el estado de la búsqueda
	 */
	public TravelSearchResponseDTO search(TravelSearchRequestDTO request) {
		TravelSearchResponseDTO response = new TravelSearchResponseDTO();
		response.setRequest(request);

		NominatimResolvedLocationDTO resolvedOrigin = nominatimService.searchCity(request.getCiudadOrigen(),
				request.getPaisOrigen());

		NominatimResolvedLocationDTO resolvedDestination = nominatimService.searchCity(request.getCiudadDestino(),
				request.getPaisDestino());

		if (Boolean.FALSE.equals(resolvedOrigin.getFound()) || Boolean.FALSE.equals(resolvedDestination.getFound())) {
			response.setSuccess(false);
			response.setMessage("No se pudo validar la ciudad o pais de origen/destino.");
			response.setResolvedOrigin(resolvedOrigin);
			response.setResolvedDestination(resolvedDestination);
			response.setOptions(List.of());
			return response;
		}

		response.setResolvedOrigin(resolvedOrigin);
		response.setResolvedDestination(resolvedDestination);

		DestinationCodeDTO destination = null;

		if (Boolean.TRUE.equals(request.getIncluirAirbnb())) {
			destination = destinationResolverService.resolve(request.getCiudadDestino(), request.getPaisDestino());
		}

		List<TravelOptionDTO> options = new ArrayList<>();

		if (Boolean.TRUE.equals(request.getIncluirVuelos())) {
			options.add(searchGoogleFlights(request, resolvedOrigin, resolvedDestination));
			options.add(mockFlightService.searchFlight(request));
		}

		if (Boolean.TRUE.equals(request.getIncluirTransporteTerrestre())) {
			options.add(terrestrialTransportService.searchRoute(request));
		}

		if (Boolean.TRUE.equals(request.getIncluirAirbnb())) {
			options.add(mockAirbnbService.searchLodging(request));
		}

		if (Boolean.TRUE.equals(request.getIncluirHoteles())) {
			options.add(buildHotelsUnavailableOption(request));
		}

		response.setSuccess(true);
		response.setMessage(buildResponseMessage(options));
		response.setOptions(options);

		return response;
	}

	/**
	 * Consulta vuelos en Google Flights resolviendo los códigos IATA de los aeropuertos
	 * de origen y destino a partir de las ubicaciones normalizadas por Nominatim.
	 *
	 * @param request             objeto con los parámetros de búsqueda
	 * @param resolvedOrigin      ubicación resuelta del origen
	 * @param resolvedDestination ubicación resuelta del destino
	 * @return {@link TravelOptionDTO} con los vuelos encontrados y el estado de la consulta
	 */
	private TravelOptionDTO searchGoogleFlights(TravelSearchRequestDTO request,
			NominatimResolvedLocationDTO resolvedOrigin, NominatimResolvedLocationDTO resolvedDestination) {

		TravelOptionDTO option = buildBaseOption(request, "GOOGLE_FLIGHTS", "FLIGHT",
				"Vuelos encontrados en Google Flights");

		AirportCodeDTO originAirport = googleFlightsAirportService.searchAirport(request.getCiudadOrigen(),
				resolvedOrigin.getCountryCode());

		AirportCodeDTO destinationAirport = googleFlightsAirportService.searchAirport(request.getCiudadDestino(),
				resolvedDestination.getCountryCode());

		if (Boolean.FALSE.equals(originAirport.getFound()) || Boolean.FALSE.equals(destinationAirport.getFound())) {
			option.setAvailable(false);
			option.setProviderSuccess(false);
			option.setProviderMessage("No se pudieron resolver aeropuertos IATA para origen o destino.");
			option.setProviderResponse("Origen: " + originAirport.getProviderResponse() + " | Destino: "
					+ destinationAirport.getProviderResponse());
			option.setPrice(null);
			option.setPriceText("Precio no disponible");
			return option;
		}

		GoogleFlightsSearchRequestDTO googleRequest = new GoogleFlightsSearchRequestDTO();
		googleRequest.setDepartureId(originAirport.getIataCode());
		googleRequest.setArrivalId(destinationAirport.getIataCode());
		googleRequest.setOutboundDate(request.getFechaSalida());
		googleRequest.setReturnDate(request.getFechaRegreso());
		googleRequest.setTravelClass(defaultString(request.getClaseViaje(), "ECONOMY"));
		googleRequest.setAdults(defaultInteger(request.getAdultos(), 1).toString());
		googleRequest.setChildren(defaultInteger(request.getNinos(), 0).toString());
		googleRequest.setInfantOnLap("0");
		googleRequest.setInfantInSeat("0");
		googleRequest.setShowHidden("1");
		googleRequest.setCurrency(defaultString(request.getMoneda(), "USD"));
		googleRequest.setLanguageCode("en-US");
		googleRequest.setCountryCode(defaultString(resolvedDestination.getCountryCode(), "US").toUpperCase());
		googleRequest.setSearchType("best");

		GoogleFlightsSearchDTO result = googleFlightsService.searchFlights(googleRequest);

		fillProviderData(option, result.getStatusCode(), result.getSuccess(), result.getMessage(),
				result.getProviderResponse(), request.getMoneda());

		return option;
	}

	/**
	 * Consulta alojamientos tipo Airbnb usando el Google Place ID del destino
	 * resuelto y los filtros del request.
	 *
	 * @param request     objeto con los parámetros de búsqueda
	 * @param destination objeto con los códigos del destino, incluyendo el Google Place ID
	 * @return {@link TravelOptionDTO} con los alojamientos encontrados y el estado de la consulta
	 */
	private TravelOptionDTO searchAirbnb(TravelSearchRequestDTO request, DestinationCodeDTO destination) {
		AirbnbSearchRequestDTO airbnbRequest = new AirbnbSearchRequestDTO();
		airbnbRequest.setPlaceId(destination.getGooglePlaceId());
		airbnbRequest.setAdults(defaultInteger(request.getAdultos(), 1));
		airbnbRequest.setGuestFavorite(false);
		airbnbRequest.setIb(false);
		airbnbRequest.setCurrency(defaultString(request.getMoneda(), "USD"));

		if (Boolean.TRUE.equals(request.getPetFriendly())) {
			airbnbRequest.setPets(Math.max(defaultInteger(request.getMascotas(), 0), 1));
		}

		AirbnbSearchDTO result = airbnbService.searchByPlaceId(airbnbRequest);

		TravelOptionDTO option = buildBaseOption(request, "AIRBNB19", "AIRBNB", "Alojamientos tipo Airbnb");
		option.setPetFriendly(Boolean.TRUE.equals(request.getPetFriendly()));
		option.setHasPool(Boolean.TRUE.equals(request.getPiscina()));
		option.setHasJacuzzi(Boolean.TRUE.equals(request.getJacuzzi()));

		fillProviderData(option, result.getStatusCode(), result.getSuccess(), result.getMessage(),
				result.getProviderResponse(), request.getMoneda());

		return option;
	}

	/**
	 * Construye una opción de hotel marcada como no disponible, indicando que
	 * Hotels4 no está activo en la cuenta actual de RapidAPI.
	 *
	 * @param request objeto con los parámetros de búsqueda
	 * @return {@link TravelOptionDTO} con disponibilidad en {@code false} y mensaje explicativo
	 */
	private TravelOptionDTO buildHotelsUnavailableOption(TravelSearchRequestDTO request) {
		return buildUnavailableOption(request, "HOTELS4", "HOTEL", "Hoteles desde Hotels4",
				"Hotels4 no esta disponible en la cuenta actual de RapidAPI o requiere busqueda previa por property id.");
	}

	/**
	 * Construye una opción de viaje marcada como no disponible con un mensaje personalizado.
	 *
	 * @param request  objeto con los parámetros de búsqueda
	 * @param provider nombre del proveedor
	 * @param type     tipo de servicio
	 * @param title    título de la opción
	 * @param message  mensaje explicativo de la no disponibilidad
	 * @return {@link TravelOptionDTO} con disponibilidad en {@code false}
	 */
	private TravelOptionDTO buildUnavailableOption(TravelSearchRequestDTO request, String provider, String type,
			String title, String message) {

		TravelOptionDTO option = buildBaseOption(request, provider, type, title);
		option.setAvailable(false);
		option.setProviderSuccess(false);
		option.setProviderStatusCode(400);
		option.setProviderMessage(message);
		option.setPrice(null);
		option.setPriceText("Precio no disponible");
		return option;
	}

	/**
	 * Construye un {@link TravelOptionDTO} base con los datos comunes del request
	 * antes de aplicar los resultados de la búsqueda.
	 *
	 * @param request  objeto con los parámetros de búsqueda
	 * @param provider nombre del proveedor
	 * @param type     tipo de servicio
	 * @param title    título de la opción
	 * @return {@link TravelOptionDTO} inicializado con los datos del request
	 */
	private TravelOptionDTO buildBaseOption(TravelSearchRequestDTO request, String provider, String type,
			String title) {

		TravelOptionDTO option = new TravelOptionDTO();
		option.setProvider(provider);
		option.setType(type);
		option.setTitle(title);
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
	 * Rellena los datos del proveedor en una opción de viaje extrayendo el precio
	 * y evaluando si la respuesta contiene errores.
	 *
	 * @param option           opción de viaje a completar
	 * @param statusCode       código de estado retornado por el proveedor
	 * @param success          indicador de éxito de la consulta al proveedor
	 * @param message          mensaje del proveedor
	 * @param providerResponse respuesta JSON del proveedor
	 * @param currency         moneda en la que se expresa el precio
	 */
	private void fillProviderData(TravelOptionDTO option, Integer statusCode, Boolean success, String message,
			String providerResponse, String currency) {

		Double price = priceExtractorService.extractFirstPrice(providerResponse);
		boolean providerHasError = hasProviderError(providerResponse);

		option.setProviderStatusCode(statusCode);
		option.setProviderSuccess(Boolean.TRUE.equals(success) && !providerHasError);
		option.setAvailable(Boolean.TRUE.equals(success) && !providerHasError);
		option.setPrice(price);
		option.setPriceText(price == null ? "Precio no disponible" : price + " " + defaultString(currency, "USD"));

		if (providerHasError) {
			option.setProviderMessage(extractProviderErrorMessage(providerResponse));
			option.setProviderResponse(providerResponse);
			return;
		}

		option.setProviderMessage(message);
		option.setProviderResponse(null);
	}

	/**
	 * Construye el mensaje general de la respuesta indicando si todos los
	 * proveedores estuvieron disponibles o si alguno presentó problemas.
	 *
	 * @param options lista de opciones de viaje retornadas por los proveedores
	 * @return mensaje descriptivo del resultado de la búsqueda
	 */
	private String buildResponseMessage(List<TravelOptionDTO> options) {
		boolean hasUnavailable = options.stream().anyMatch(option -> !Boolean.TRUE.equals(option.getAvailable()));

		if (hasUnavailable) {
			return "Busqueda finalizada con algunos proveedores no disponibles.";
		}

		return "Busqueda finalizada exitosamente.";
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

	/**
	 * Evalúa si la respuesta JSON de un proveedor contiene indicadores de error,
	 * verificando campos como {@code error}, {@code status} y {@code __typename}.
	 *
	 * @param providerResponse respuesta JSON del proveedor a evaluar
	 * @return {@code true} si la respuesta contiene un error, {@code false} en caso contrario
	 */
	private boolean hasProviderError(String providerResponse) {
		if (providerResponse == null || providerResponse.isBlank()) {
			return true;
		}

		try {
			com.google.gson.JsonObject root = com.google.gson.JsonParser.parseString(providerResponse)
					.getAsJsonObject();

			if (root.has("__typename") && "AppError".equalsIgnoreCase(root.get("__typename").getAsString())) {
				return true;
			}

			if (root.has("status") && !root.get("status").isJsonNull() && !root.get("status").getAsBoolean()) {
				return true;
			}

			if (root.has("error") && !root.get("error").isJsonNull()) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Extrae el mensaje de error desde la respuesta JSON de un proveedor,
	 * buscando los campos {@code error} y {@code message}.
	 *
	 * @param providerResponse respuesta JSON del proveedor
	 * @return mensaje de error extraído, o un mensaje genérico si no puede obtenerse
	 */
	private String extractProviderErrorMessage(String providerResponse) {
		try {
			JsonObject root = JsonParser.parseString(providerResponse)
					.getAsJsonObject();

			if (root.has("error") && !root.get("error").isJsonNull()) {
				return root.get("error").getAsString();
			}

			if (root.has("message") && !root.get("message").isJsonNull()) {
				return root.get("message").toString();
			}
		} catch (Exception e) {
			return "Error del proveedor externo.";
		}

		return "Error del proveedor externo.";
	}
}