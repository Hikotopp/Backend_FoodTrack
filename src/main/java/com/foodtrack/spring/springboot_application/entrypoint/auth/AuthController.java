package com.foodtrack.spring.springboot_application.entrypoint.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final Map<String, AuthUser> users = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PostConstruct
    public void init() {
        String adminPassword = passwordEncoder.encode("admin");
        users.put("admin", new AuthUser("admin", "admin@example.com", adminPassword, "ADMIN", "Administrador"));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        if (request == null || request.getUsuario() == null || request.getContrasena() == null) {
            return ResponseEntity.badRequest().build();
        }

        AuthUser user = users.get(request.getUsuario());
        if (user == null || !passwordEncoder.matches(request.getContrasena(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = generateToken(user.getUsername());
        LoginResponse response = new LoginResponse(token, user.getRol(), user.getNombre(), user.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/usuarios")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        if (request == null || request.getUsername() == null || request.getContrasena() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (users.containsKey(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String encodedPassword = passwordEncoder.encode(request.getContrasena());
        AuthUser newUser = new AuthUser(request.getUsername(), request.getEmail(), encodedPassword, request.getRol() == null ? "USER" : request.getRol(), request.getUsername());
        users.put(request.getUsername(), newUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}
