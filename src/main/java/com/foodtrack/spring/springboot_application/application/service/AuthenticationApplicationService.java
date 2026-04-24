package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.AuthenticationView;
import com.foodtrack.spring.springboot_application.application.model.UserProfileView;
import com.foodtrack.spring.springboot_application.application.port.in.AuthUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthenticationApplicationService implements AuthUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationApplicationService(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticationView register(String fullName, String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);
        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new BusinessRuleException("Email is already registered.");
        }

        AppUser userToSave = new AppUser(
                null,
                fullName.trim(),
                normalizedEmail,
                passwordEncoder.encode(rawPassword),
                UserRole.EMPLOYEE
        );

        AppUser savedUser = userRepositoryPort.save(userToSave);
        return toAuthenticationView(savedUser);
    }

    @Override
    public AuthenticationView login(String email, String rawPassword) {
        AppUser user = userRepositoryPort.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new BusinessRuleException("Invalid credentials."));

        if (!passwordEncoder.matches(rawPassword, user.passwordHash())) {
            throw new BusinessRuleException("Invalid credentials.");
        }

        return toAuthenticationView(user);
    }

    @Override
    public UserProfileView getProfile(String email) {
        AppUser user = userRepositoryPort.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found."));

        return new UserProfileView(user.id(), user.fullName(), user.email(), user.role());
    }

    private AuthenticationView toAuthenticationView(AppUser user) {
        return new AuthenticationView(
                jwtService.generateToken(user),
                user.fullName(),
                user.email(),
                user.role()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
