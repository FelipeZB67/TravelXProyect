package co.edu.unbosque.travelx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.service.AirLabsService;

@RestController
public class AirLabsController {

    @Autowired
    private AirLabsService airLabsService;

    @GetMapping("/airlabs/transbordos/{indiceOrigen}/{indiceDestino}")
    public int obtenerCantidadTransbordos(
            @RequestParam int indiceOrigen,
            @RequestParam int indiceDestino) {

        return airLabsService.obtenerCantidadTransbordos(indiceOrigen, indiceDestino);
    }
    
    @GetMapping("/airlabs/codigos-iata")
    public List<String> obtenerCodigosIata() {
        return airLabsService.obtenerCodigosIata();
    }
}