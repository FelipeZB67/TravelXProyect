package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.NominatimLocationDTO;

@Service
public class NominatimService {

    public List<NominatimLocationDTO> obtenerHotelesPorPais(int indicePais) {
        return ExternalHTTPRequestHandler.doGetLocations(indicePais);
    }

    public List<String> obtenerCodigosPaises() {
        return ExternalHTTPRequestHandler.CODIGOS;
    }
}