package com.foodtrack.spring.springboot_application.entrypoint.auth;

public class LoginResponse {
    private String token;
    private String tipoUsuario;
    private String nombre;
    private String usuario;

    public LoginResponse(String token, String tipoUsuario, String nombre, String usuario) {
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
