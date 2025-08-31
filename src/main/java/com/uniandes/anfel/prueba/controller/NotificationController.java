package com.uniandes.anfel.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.uniandes.anfel.prueba.services.NotificationRequest;
import com.uniandes.anfel.prueba.queue.NotificationQueue;

@RestController
public class NotificationController {

    @Autowired
    private NotificationQueue notificationQueue;

    @PostMapping("/send-notification")
    public String handleNotification(@RequestBody NotificationRequest request) {
        notificationQueue.push(request);
        return "Notificación encolada";
    }
}
