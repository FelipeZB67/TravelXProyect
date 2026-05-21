package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.travelx.dto.AirLabsRouteDTO;
import co.edu.unbosque.travelx.dto.AirLabsRoutesResponseDTO;
import co.edu.unbosque.travelx.dto.DistanceMatrixResponseDTO;
import co.edu.unbosque.travelx.dto.NominatimLocationDTO;
import co.edu.unbosque.travelx.dto.OverpassElementDTO;
import co.edu.unbosque.travelx.dto.OverpassResponseDTO;

public class ExternalHTTPRequestHandler {

	public static final String DISTANCE_MATRIX_API_KEY = "kX0YNyHdVuxclCCnPPRTneQZGWmJaa9WOuXAFn8ut6gTj7zVeIcN2ziqmJULgxra";

	public static final String AIRLABS_API_KEY = "b969c135-3c53-4afa-8fdf-41a07dd5dc23";

	public static final List<String> MODOS_TRANSPORTE = List.of("driving", "walking", "bicycling", "transit");

	public static final List<String> CODIGOS = List.of("co", "mx", "ar", "br", "cl", "pe", "ve", "ec", "bo", "uy", "py",
			"cr", "pa", "es", "us", "fr", "it", "de", "jp", "cn", "in", "au");

	public static final List<String> CODIGOS_IATA = List.of("BOG", "MDE", "CLO", "CTG", "BAQ", "SMR", "PEI", "BGA",
			"ADZ", "MIA", "JFK", "LAX", "ORD", "ATL", "DFW", "IAH", "SFO", "BOS", "MEX", "CUN", "GDL", "MAD", "BCN",
			"CDG", "ORY", "LHR", "FCO", "BER", "AMS", "EZE", "SCL", "LIM", "UIO", "GYE", "CCS", "PTY", "SJO", "MVD",
			"ASU", "VVI", "GRU", "GIG", "NRT", "PEK", "PVG", "DEL", "SYD");

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	// Devuelve el JSON crudo como String
	public static String doGet(String url) {
		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader("User-Agent", "Java 11 HttpClients").build();

		HttpResponse<String> respuesta = null;

		try {
			respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			Thread.currentThread().interrupt();
		}

		if (respuesta == null) {
			return "";
		}

		return respuesta.statusCode() + "\n" + respuesta.body();
	}

	// Devuelve lista de ubicaciones desde Nominatim
	public static List<NominatimLocationDTO> doGetLocations(int indiceLocation) {
		String countryCode = CODIGOS.get(indiceLocation);

		String url = "https://nominatim.openstreetmap.org/search?q=hotel&format=json" + "&limit=30&countrycodes="
				+ countryCode;

		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)").build();

		HttpResponse<String> respuesta = null;

		try {
			respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			Thread.currentThread().interrupt();
		}

		if (respuesta == null) {
			return List.of();
		}

		Gson gson = new Gson();
		Type tipoLista = new TypeToken<List<NominatimLocationDTO>>() {
		}.getType();

		return gson.fromJson(respuesta.body(), tipoLista);
	}

	// Devuelve respuesta completa desde Overpass
	public static OverpassResponseDTO doGetOverpassHotels(int indiceLocation) {
		String codigoPais = CODIGOS.get(indiceLocation).toUpperCase();

		String url = "https://overpass-api.de/api/interpreter?data=%5Bout%3Ajson%5D%3Barea%5B%22ISO3166-1%22%3D%22"
				+ codigoPais
				+ "%22%5D-%3E.a%3B%28node%5B%22tourism%22%3D%22hotel%22%5D%28area.a%29%3Bway%5B%22tourism%22%3D%22hotel%22%5D%28area.a%29%3B%29%3Bout%20center%2030%3B";

		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)").build();

		HttpResponse<String> respuesta = null;

		try {
			respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			Thread.currentThread().interrupt();
		}

		if (respuesta == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(respuesta.body(), OverpassResponseDTO.class);
	}

	// Devuelve solo la lista de hoteles desde Overpass
	public static List<OverpassElementDTO> doGetOverpassHotelElements(int indiceLocation) {
		OverpassResponseDTO response = doGetOverpassHotels(indiceLocation);

		if (response == null || response.getElements() == null) {
			return List.of();
		}

		return response.getElements();
	}

	// Devuelve respuesta completa desde AirLabs Routes
	public static AirLabsRoutesResponseDTO doGetAirLabsRoutes(int indiceOrigen, int indiceDestino, int limit) {
		if (limit <= 0) {
			limit = 20;
		}

		String depIata = CODIGOS_IATA.get(indiceOrigen);
		String arrIata = CODIGOS_IATA.get(indiceDestino);

		String url = "https://airlabs.co/api/v9/routes" + "?api_key=" + AIRLABS_API_KEY + "&dep_iata=" + depIata
				+ "&arr_iata=" + arrIata + "&limit=" + limit;

		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)").build();

		HttpResponse<String> respuesta = null;

		try {
			respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			Thread.currentThread().interrupt();
		}

		if (respuesta == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(respuesta.body(), AirLabsRoutesResponseDTO.class);
	}

	// Devuelve solo la lista de rutas/vuelos desde AirLabs
	public static List<AirLabsRouteDTO> doGetAirLabsRouteElements(int indiceOrigen, int indiceDestino, int limit) {
		AirLabsRoutesResponseDTO response = doGetAirLabsRoutes(indiceOrigen, indiceDestino, limit);

		if (response == null || response.getResponse() == null) {
			return List.of();
		}

		return response.getResponse();
	}

	public static int doGetAirLabsRoutesCount(int indiceOrigen, int indiceDestino) {
		List<AirLabsRouteDTO> rutas = doGetAirLabsRouteElements(indiceOrigen, indiceDestino, 50);
		return rutas.size();
	}

	public static DistanceMatrixResponseDTO doGetDistanceMatrix(int indiceOrigen, int indiceDestino,
			int indiceModoTransporte) {
		String origen = CODIGOS_IATA.get(indiceOrigen);
		String destino = CODIGOS_IATA.get(indiceDestino);
		String modoTransporte = MODOS_TRANSPORTE.get(indiceModoTransporte);

		String url = "https://api.distancematrix.ai/maps/api/distancematrix/json" + "?origins=" + origen
				+ "&destinations=" + destino + "&mode=" + modoTransporte + "&key=" + DISTANCE_MATRIX_API_KEY;

		HttpRequest solicitud = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)").build();

		HttpResponse<String> respuesta = null;

		try {
			respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			Thread.currentThread().interrupt();
		}

		if (respuesta == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(respuesta.body(), DistanceMatrixResponseDTO.class);
	}
}