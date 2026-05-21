package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.dto.DistanceMatrixResponseDTO;
import co.edu.unbosque.travelx.service.DistanceMatrixService;

@RestController
public class DistanceMatrixController {

	@Autowired
	private DistanceMatrixService distanceMatrixService;

	@GetMapping("/distance-matrix/{indiceOrigen}/{indiceDestino}/{indiceModoTransporte}")
	public DistanceMatrixResponseDTO obtenerMatrizDistancia(@RequestParam int indiceOrigen,
			@RequestParam int indiceDestino, @RequestParam int indiceModoTransporte) {

		return distanceMatrixService.obtenerMatrizDistancia(indiceOrigen, indiceDestino, indiceModoTransporte);
	}

	@GetMapping("/distance-matrix/modos-transporte")
	public List<String> obtenerModosTransporte() {
		return distanceMatrixService.obtenerModosTransporte();
	}

	@GetMapping("/distance-matrix/codigos-iata")
	public List<String> obtenerCodigosIata() {
		return distanceMatrixService.obtenerCodigosIata();
	}
}