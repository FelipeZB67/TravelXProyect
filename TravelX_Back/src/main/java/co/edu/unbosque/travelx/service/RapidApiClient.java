package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Service;

@Service
public class RapidApiClient {

	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(15))
			.build();

	public String doGet(String url, String rapidApiHost, String rapidApiKey) {
		if (rapidApiKey == null || rapidApiKey.isBlank()) {
			return "{ \"error\": \"No existe API key para el host: " + rapidApiHost + "\" }";
		}

		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(30))
				.header("Content-Type", "application/json")
				.header("x-rapidapi-key", rapidApiKey)
				.header("x-rapidapi-host", rapidApiHost)
				.build();

		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() < 200 || response.statusCode() >= 300) {
				return "{ \"statusCode\": " + response.statusCode()
						+ ", \"error\": " + new com.google.gson.Gson().toJson(response.body())
						+ " }";
			}

			return response.body();
		} catch (IOException e) {
			return "{ \"error\": \"Error de conexion: " + e.getMessage() + "\" }";
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return "{ \"error\": \"Peticion interrumpida: " + e.getMessage() + "\" }";
		}
	}
}