package com.uniandes.anfel.prueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotificationService {

    private static final int RETARDO_MS = 200; // configurable
    private static final int MAX_LATENCIA_MS = 1000;
    private static final String LOG_FILE = "notificaciones.csv";

    private static final AtomicInteger totalNotificaciones = new AtomicInteger(0);
    private static final AtomicInteger notificacionesCumplen = new AtomicInteger(0);
    private static final AtomicLong sumaLatencias = new AtomicLong(0);
    private static final AtomicLong latenciaMaxima = new AtomicLong(0);

    private static final Object lock = new Object();

    @Autowired
    private ExecutorService pool;

    public NotificationService() {
        inicializarArchivo();
    }

    public void processNotification(NotificationRequest request) {
        long inicio = System.currentTimeMillis();
        pool.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(50) + 50);
                sendNotification("Seller", request.getSellerUserIp(), request.getNotificationId(), request.getNotificationMessage(), inicio);
                sendNotification("Buyer", request.getBuyerUserIp(), request.getNotificationId(), request.getNotificationMessage(), inicio);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void sendNotification(String destinatario, String userIp, String notificationId, String notificationMessage, long inicio) {
        try {
            Thread.sleep(RETARDO_MS);
            long fin = System.currentTimeMillis();
            long latencia = fin - inicio;
            totalNotificaciones.incrementAndGet();
            sumaLatencias.addAndGet(latencia);
            latenciaMaxima.updateAndGet(x -> Math.max(x, latencia));
            if (latencia <= MAX_LATENCIA_MS) {
                notificacionesCumplen.incrementAndGet();
            }
            // Guardar en archivo
            synchronized (lock) {
                try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
                    fw.write(destinatario + "," + userIp + "," + notificationId + "," + notificationMessage + "," + latencia + "," + fin + "\n");
                }
            }
            System.out.println("Notificacion enviada a " + destinatario + " (" + userIp + ") en " + latencia + " ms: " + notificationMessage);
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void inicializarArchivo() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            fw.write("destinatario,latencia_ms,timestamp\n");
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando log", e);
        }
    }
}
