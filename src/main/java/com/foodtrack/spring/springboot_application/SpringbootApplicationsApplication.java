package com.foodtrack.spring.springboot_application;

// Importaciones necesarias
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication = Configuracion automatica de Spring Boot
// Incluye: @Configuration, @EnableAutoConfiguration, @ComponentScan
@SpringBootApplication
public class SpringbootApplicationsApplication {

    // Metodo main - Punto de entrada de la aplicacion
    public static void main(String[] args) {
        // Inicia la aplicacion Spring Boot
        SpringApplication.run(SpringbootApplicationsApplication.class, args);
        // Mensaje de confirmacion en consola
        System.out.println("FoodTrack API iniciada correctamente!");
        System.out.println("Accede a: http://localhost:8080");
    }
}