package co.edu.unbosque.travelx.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Service;

/**
 * Cliente HTTP simplificado para realizar peticiones GET sin autenticación,
 * gestionando errores de conexión y respuestas no exitosas.
 */
@Service
public class SimpleHttpClientService {

	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(15))
			.build();

	/**
	 * Realiza una petición GET a la URL indicada y retorna el cuerpo de la respuesta.
	 *
	 * @param url URL completa del endpoint a consultar
	 * @return cuerpo de la respuesta en formato JSON, o un JSON de error si
	 *         la respuesta no es exitosa o hay un fallo de conexión
	 */
	public String doGet(String url) {
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(30))
				.header("Content-Type", "application/json")
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