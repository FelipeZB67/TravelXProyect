package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.AirLabsRouteDTO;

@Service
public class AirLabsService {

    public int obtenerCantidadTransbordos(int indiceOrigen, int indiceDestino) {
        if (indiceOrigen == indiceDestino) {
            return 0;
        }

        List<AirLabsRouteDTO> rutasDirectas = ExternalHTTPRequestHandler
                .doGetAirLabsRouteElements(indiceOrigen, indiceDestino, 20);

        if (rutasDirectas == null || rutasDirectas.isEmpty()) {
            return 1;
        }

        return 0;
    }
}