package com.uniandes.anfel.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class PruebaApplication {
    
    private static final int NUM_HILOS = 10; // configurable

    public static void main(String[] args) {
        SpringApplication.run(PruebaApplication.class, args);
        System.out.println("Backend escuchando en http://localhost:8080/emparejamiento");
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(NUM_HILOS);
    }

}
