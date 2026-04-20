package com.foodtrack.spring.springboot_application.entrypoint.auth;

public class AuthUser {
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String rol;
    private final String nombre;

    public AuthUser(String username, String email, String passwordHash, String rol, String nombre) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }
}
