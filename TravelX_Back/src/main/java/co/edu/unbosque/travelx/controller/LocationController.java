package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unbosque.travelx.dto.NominatimLocationDTO;
import co.edu.unbosque.travelx.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // GET /api/locations/codigos
    // Devuelve la lista de códigos disponibles
    @GetMapping("/codigos")
    public ResponseEntity<List<String>> getCodigos() {
        return ResponseEntity.ok(locationService.getCodigos());
    }

    // GET /api/locations/buscar?query=hotel&indicePais=0&limit=40
    // Busca por índice (0 = co, 1 = mx, etc.)
    @GetMapping("/buscar")
    public ResponseEntity<List<NominatimLocationDTO>> buscarPorIndice(
            @RequestParam String query,
            @RequestParam int indicePais,
            @RequestParam(defaultValue = "40") int limit) {

        List<NominatimLocationDTO> resultados =
            locationService.buscarPorPais(indicePais, limit);
        return ResponseEntity.ok(resultados);
    }

    // GET /api/locations/buscar/co?query=hotel&limit=40
    // Busca por código directo
    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<List<NominatimLocationDTO>> buscarPorCodigo(
            @PathVariable String codigo,
            @RequestParam String query) {

        List<NominatimLocationDTO> resultados =
            locationService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(resultados);
    }
}