package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.dto.NominatimLocationDTO;
import co.edu.unbosque.travelx.service.NominatimService;

@RestController
public class NominatimController {

    @Autowired
    private NominatimService nominatimService;

    @GetMapping("/nominatim/hoteles/{indicePais}")
    public List<NominatimLocationDTO> obtenerHotelesPorPais(@RequestParam int indicePais) {
        return nominatimService.obtenerHotelesPorPais(indicePais);
    }

    @GetMapping("/nominatim/hoteles/{indicePais}/{limite}")
    public List<NominatimLocationDTO> obtenerHotelesPorPaisConLimite(
            @RequestParam int indicePais) {

        return nominatimService.obtenerHotelesPorPais(indicePais);
    }

    @GetMapping("/nominatim/codigos-paises")
    public List<String> obtenerCodigosPaises() {
        return nominatimService.obtenerCodigosPaises();
    }
}