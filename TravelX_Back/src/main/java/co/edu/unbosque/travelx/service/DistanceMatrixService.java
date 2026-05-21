package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.DistanceMatrixResponseDTO;

@Service
public class DistanceMatrixService {

	public DistanceMatrixResponseDTO obtenerMatrizDistancia(int indiceOrigen, int indiceDestino,
			int indiceModoTransporte) {

		return ExternalHTTPRequestHandler.doGetDistanceMatrix(indiceOrigen, indiceDestino, indiceModoTransporte);
	}

	public List<String> obtenerModosTransporte() {
		return ExternalHTTPRequestHandler.MODOS_TRANSPORTE;
	}

	public List<String> obtenerCodigosIata() {
		return ExternalHTTPRequestHandler.CODIGOS_IATA;
	}
}