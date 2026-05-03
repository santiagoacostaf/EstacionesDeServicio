package com.terpel.estacionesdeservicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EstacionesDeServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstacionesDeServicioApplication.class, args);
    }

}
