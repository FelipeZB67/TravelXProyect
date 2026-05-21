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

import co.edu.unbosque.travelx.dto.NominatimLocationDTO;
import co.edu.unbosque.travelx.dto.OverpassElementDTO;
import co.edu.unbosque.travelx.dto.OverpassResponseDTO;

public class ExternalHTTPRequestHandler {

    public static final List<String> CODIGOS = List.of(
            "co", "mx", "ar", "br", "cl", "pe", "ve", "ec", "bo", "uy", "py", "cr",
            "pa", "es", "us", "fr", "it", "de", "jp", "cn", "in", "au"
    );

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // Devuelve el JSON crudo como String
    public static String doGet(String url) {
        HttpRequest solicitud = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClients")
                .build();

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
    public static List<NominatimLocationDTO> doGetLocations(int indiceLocation, int limit) {
        String countryCode = CODIGOS.get(indiceLocation);

        String url = "https://nominatim.openstreetmap.org/search?q=hotel&format=json"
                + "&limit=" + limit
                + "&countrycodes=" + countryCode;

        HttpRequest solicitud = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)")
                .build();

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
        Type tipoLista = new TypeToken<List<NominatimLocationDTO>>() {}.getType();

        return gson.fromJson(respuesta.body(), tipoLista);
    }

    // Devuelve respuesta completa desde Overpass
    public static OverpassResponseDTO doGetOverpassHotels(int indiceLocation) {
        String codigoPais = CODIGOS.get(indiceLocation).toUpperCase();

        String url = "https://overpass-api.de/api/interpreter?data="
                + "%5Bout%3Ajson%5D%3B"
                + "area%5B%22ISO3166-1%22%3D%22" + codigoPais + "%22%5D-%3E.a%3B"
                + "%28"
                + "node%5B%22tourism%22%3D%22hotel%22%5D%28area.a%29%3B"
                + "way%5B%22tourism%22%3D%22hotel%22%5D%28area.a%29%3B"
                + "%29%3B"
                + "out%20center%2030%3B";

        HttpRequest solicitud = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)")
                .build();

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
}