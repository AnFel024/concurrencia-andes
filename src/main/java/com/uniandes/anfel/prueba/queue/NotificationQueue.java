package com.uniandes.anfel.prueba.queue;

import com.uniandes.anfel.prueba.services.NotificationRequest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

@Component
public class NotificationQueue {
    private final BlockingQueue<NotificationRequest> queue = new LinkedBlockingQueue<>();

    public void push(NotificationRequest request) {
        System.out.println("Push a la cola: " + request);
        queue.offer(request);
    }

    public NotificationRequest pull() throws InterruptedException {
        NotificationRequest req = queue.take();
        System.out.println("Pull de la cola: " + req);
        return req;
    }
}
