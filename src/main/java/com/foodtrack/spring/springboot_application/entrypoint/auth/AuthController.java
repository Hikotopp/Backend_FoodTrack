package com.foodtrack.spring.springboot_application.entrypoint.auth;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.model.UserProfileView;
import com.foodtrack.spring.springboot_application.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new employee account")
    public ResponseEntity<AuthenticationView> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationView response = authUseCase.register(request.fullName(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and return a JWT token")
    public ResponseEntity<AuthenticationView> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authUseCase.login(request.email(), request.password()));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated user profile")
    public ResponseEntity<UserProfileView> me(Authentication authentication) {
        return ResponseEntity.ok(authUseCase.getProfile(authentication.getName()));
    }
}
