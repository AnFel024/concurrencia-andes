package com.uniandes.anfel.prueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.uniandes.anfel.prueba.queue.NotificationQueue;

import jakarta.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NotificationService {

    private static final int RETARDO_MS = 100; // configurable
    private static final int MAX_LATENCIA_MS = 1000;
    private static final String LOG_FILE = "notificaciones.csv";

    private static final AtomicInteger totalNotificaciones = new AtomicInteger(0);
    private static final AtomicInteger notificacionesCumplen = new AtomicInteger(0);
    private static final AtomicLong sumaLatencias = new AtomicLong(0);
    private static final AtomicLong latenciaMaxima = new AtomicLong(0);

    private static final Object lock = new Object();

    @Autowired
    private NotificationQueue notificationQueue;

    // Inyectamos el bean definido en PruebaApplication
    private final ExecutorService pool;

    public NotificationService(ExecutorService pool) {
        this.pool = pool;
    }

    @PostConstruct
    public void init() {
        inicializarArchivo();
        System.out.println("Iniciando consumidores de la cola...");
        
        // Lanzamos un consumidor por cada hilo disponible en el pool
        int numHilos = ((java.util.concurrent.ThreadPoolExecutor) pool).getCorePoolSize();
        for (int i = 0; i < numHilos; i++) {
            pool.submit(this::consumeQueue);
        }
    }

    private void consumeQueue() {
        System.out.println("Consumidor iniciado en hilo: " + Thread.currentThread().getName());
        while (true) {
            try {
                NotificationRequest request = notificationQueue.pull();
                processNotification(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void processNotification(NotificationRequest request) {
        // System.out.println("Procesando notificación: " + request);
        long inicio = System.currentTimeMillis();
        try {
            Thread.sleep(RETARDO_MS);
            long fin = System.currentTimeMillis();
            long latencia = fin - inicio;

            sendNotification("Seller", request.getSellerUserIp(), request.getNotificationId(), request.getNotificationMessage());
            sendNotification("Buyer", request.getBuyerUserIp(), request.getNotificationId(), request.getNotificationMessage());

            totalNotificaciones.addAndGet(2);
            sumaLatencias.addAndGet(latencia);
            latenciaMaxima.updateAndGet(x -> Math.max(x, latencia));
            if (latencia <= MAX_LATENCIA_MS) {
                notificacionesCumplen.addAndGet(2);
            }

            // Guardar en archivo
            synchronized (lock) {
                try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
                    fw.write("Seller," + request.getSellerUserIp() + "," + request.getNotificationId() + "," + request.getNotificationMessage() + "," + latencia + "," + fin + "\n");
                    fw.write("Buyer," + request.getBuyerUserIp() + "," + request.getNotificationId() + "," + request.getNotificationMessage() + "," + latencia + "," + fin + "\n");
                }
            }

            System.out.println("Notificaciones enviadas a Seller y Buyer en " + latencia + " ms: " + request.getNotificationMessage());
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendNotification(String destinatario, String userIp, String notificationId, String notificationMessage) {
        // Simula el envío, pero no espera ni calcula latencia individual
        // System.out.println("Enviando notificación a " + destinatario + " (" + userIp + "): " + notificationMessage);
    }

    private void inicializarArchivo() {
        try (FileWriter fw = new FileWriter(LOG_FILE, false)) {
            fw.write("destinatario,userIp,notificationId,mensaje,latencia_ms,timestamp\n");
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando log", e);
        }
    }
}
