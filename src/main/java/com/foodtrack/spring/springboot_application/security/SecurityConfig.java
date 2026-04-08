package com.foodtrack.spring.springboot_application.security;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

// @Configuration = Clase de configuracion de Spring
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;  // Inyectar el filtro JWT

    // Registrar el filtro JWT para que se ejecute en todas las peticiones
    // FilterRegistrationBean = Registra un filtro en el contenedor de Servlets
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        // Crear el registro del filtro
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        
        // Establecer el filtro
        registrationBean.setFilter(jwtFilter);
        
        // Aplicar a todas las URLs
        registrationBean.addUrlPatterns("/*");
        
        // Alta prioridad - ejecutar antes que otros filtros
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        
        return registrationBean;
    }
}