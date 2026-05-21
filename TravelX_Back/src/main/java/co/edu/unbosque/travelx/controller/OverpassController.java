package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.dto.OverpassElementDTO;
import co.edu.unbosque.travelx.dto.OverpassResponseDTO;
import co.edu.unbosque.travelx.service.OverpassService;

@RestController
public class OverpassController {

    @Autowired
    private OverpassService overpassService;

    @GetMapping("/overpass/hoteles/{indicePais}")
    public List<OverpassElementDTO> obtenerHotelesPorPais(@PathVariable int indicePais) {
        return overpassService.obtenerHotelesPorPais(indicePais);
    }

    @GetMapping("/overpass/hoteles/respuesta-completa/{indicePais}")
    public OverpassResponseDTO obtenerRespuestaCompletaHotelesPorPais(@PathVariable int indicePais) {
        return overpassService.obtenerRespuestaCompletaHotelesPorPais(indicePais);
    }

    @GetMapping("/overpass/codigos-paises")
    public List<String> obtenerCodigosPaises() {
        return overpassService.obtenerCodigosPaises();
    }
}