package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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

	private TravelOptionDTO buildHotelsUnavailableOption(TravelSearchRequestDTO request) {
		return buildUnavailableOption(request, "HOTELS4", "HOTEL", "Hoteles desde Hotels4",
				"Hotels4 no esta disponible en la cuenta actual de RapidAPI o requiere busqueda previa por property id.");
	}

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

	private String buildResponseMessage(List<TravelOptionDTO> options) {
		boolean hasUnavailable = options.stream().anyMatch(option -> !Boolean.TRUE.equals(option.getAvailable()));

		if (hasUnavailable) {
			return "Busqueda finalizada con algunos proveedores no disponibles.";
		}

		return "Busqueda finalizada exitosamente.";
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

	private String extractProviderErrorMessage(String providerResponse) {
		try {
			com.google.gson.JsonObject root = com.google.gson.JsonParser.parseString(providerResponse)
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