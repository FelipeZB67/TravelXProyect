package co.edu.unbosque.travelx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.service.AirLabsService;

@RestController
public class AirLabsController {

    @Autowired
    private AirLabsService airLabsService;

    @GetMapping("/airlabs/transbordos/{indiceOrigen}/{indiceDestino}")
    public int obtenerCantidadTransbordos(
            @PathVariable int indiceOrigen,
            @PathVariable int indiceDestino) {

        return airLabsService.obtenerCantidadTransbordos(indiceOrigen, indiceDestino);
    }
}