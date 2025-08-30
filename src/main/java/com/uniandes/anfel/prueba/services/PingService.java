package com.uniandes.anfel.prueba.services;

import org.springframework.stereotype.Service;

@Service
public class PingService {

    public String pong() {
        return "pong";
    }
}
