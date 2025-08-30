package com.uniandes.anfel.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uniandes.anfel.prueba.services.NotificationRequest;
import com.uniandes.anfel.prueba.services.NotificationService;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService emparejamientoService;

    @PostMapping("/send-notification")
    public String processNotification(@RequestBody NotificationRequest request) {
        emparejamientoService.processNotification(request);
        return "Notificación recibida";
    }
}
