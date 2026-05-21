package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.travelx.dto.NominatimLocationDTO;

import java.lang.reflect.Type;
import java.util.List;


public class ExternalHTTPRequestHandler {

	public static final List<String> CODIGOS = List.of("co", "mx", "ar", "br", "cl", "pe", "ve", "ec", "bo", "uy", "py", "cr", "pa", "es", "us", "fr", "it", "de", "jp", "cn", "in", "au");

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
        }

        return respuesta.statusCode() + "\n" + respuesta.body();
    }

    // Devuelve lista de ubicaciones desde Nominatim
    public static List<NominatimLocationDTO> doGetLocations(int indiceLocation, int limit) {
    	String countryCode = CODIGOS.get(indiceLocation);

        // Construye la URL dinámicamente
        String url = "https://nominatim.openstreetmap.org/search?q=hotel&format=json"
                   + "&limit=" + limit
                   + "&countrycodes=" + countryCode;

        HttpRequest solicitud = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                // Nominatim exige un User-Agent descriptivo
                .setHeader("User-Agent", "MiAppSpring/1.0 (correo@ejemplo.com)")
                .build();

        HttpResponse<String> respuesta = null;

        try {
            respuesta = HTTP_CLIENT.send(solicitud, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Gson necesita TypeToken para deserializar List<T>
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<NominatimLocationDTO>>() {}.getType();

        return gson.fromJson(respuesta.body(), tipoLista);
    }
}