package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.OverpassElementDTO;
import co.edu.unbosque.travelx.dto.OverpassResponseDTO;

@Service
public class OverpassService {

    public OverpassResponseDTO obtenerRespuestaCompletaHotelesPorPais(int indicePais) {
        return ExternalHTTPRequestHandler.doGetOverpassHotels(indicePais);
    }

    public List<OverpassElementDTO> obtenerHotelesPorPais(int indicePais) {
        return ExternalHTTPRequestHandler.doGetOverpassHotelElements(indicePais);
    }

    public List<String> obtenerCodigosPaises() {
        return ExternalHTTPRequestHandler.CODIGOS;
    }
}