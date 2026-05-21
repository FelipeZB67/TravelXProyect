package co.edu.unbosque.travelx.service;

import java.util.List;
import co.edu.unbosque.travelx.dto.NominatimLocationDTO;

import org.springframework.stereotype.Service;

@Service
public class LocationService {

    // Expone la lista de códigos para el controller
    public List<String> getCodigos() {
        return ExternalHTTPRequestHandler.CODIGOS;
    }

    // Busca por índice del país
    public List<NominatimLocationDTO> buscarPorPais(int indicePais, int limit) {
        // Valida que el índice esté dentro del rango
        if (indicePais < 0 || indicePais >= ExternalHTTPRequestHandler.CODIGOS.size()) {
            throw new IllegalArgumentException("Índice de país inválido: " + indicePais);
        }
        return ExternalHTTPRequestHandler.doGetLocations(indicePais, limit);
    }

    // Busca por código directo (ej: "co", "us")
    public List<NominatimLocationDTO> buscarPorCodigo(String codigo, int limit) {
        if (!ExternalHTTPRequestHandler.CODIGOS.contains(codigo)) {
            throw new IllegalArgumentException("Código de país no soportado: " + codigo);
        }
        int indice = ExternalHTTPRequestHandler.CODIGOS.indexOf(codigo);
        return ExternalHTTPRequestHandler.doGetLocations(indice, limit);
    }
}