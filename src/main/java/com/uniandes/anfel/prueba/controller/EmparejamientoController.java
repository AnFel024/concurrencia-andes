package com.uniandes.anfel.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uniandes.anfel.prueba.services.EmparejamientoService;

@RestController
public class EmparejamientoController {

    @Autowired
    private EmparejamientoService emparejamientoService;

    @PostMapping("/emparejamiento")
    public String recibirEmparejamiento() {
        long inicioEmparejamiento = System.currentTimeMillis();
        emparejamientoService.procesarEmparejamiento(inicioEmparejamiento);
        return "Emparejamiento recibido";
    }
}
