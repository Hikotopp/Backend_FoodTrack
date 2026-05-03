package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.UserAccountView;
import com.foodtrack.spring.springboot_application.application.port.in.UserAdministrationUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.JpaCustomerOrderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class UserAdministrationApplicationService implements UserAdministrationUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final JpaCustomerOrderRepository customerOrderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdministrationApplicationService(
            UserRepositoryPort userRepositoryPort,
            JpaCustomerOrderRepository customerOrderRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.customerOrderRepository = customerOrderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccountView createUser(String fullName, String email, String rawPassword, UserRole role) {
        String normalizedEmail = normalizeEmail(email);
        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new BusinessRuleException("Email is already registered.");
        }

        AppUser saved = userRepositoryPort.save(new AppUser(
                null,
                fullName.trim(),
                normalizedEmail,
                passwordEncoder.encode(rawPassword),
                role
        ));
        return toView(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAccountView> listUsers() {
        return userRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(AppUser::fullName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toView)
                .toList();
    }

    @Override
    public UserAccountView updateRole(Long userId, UserRole role) {
        AppUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found."));

        AppUser updated = userRepositoryPort.save(new AppUser(
                user.id(),
                user.fullName(),
                user.email(),
                user.passwordHash(),
                role
        ));
        return toView(updated);
    }

    @Override
    public void deleteUser(Long userId, String currentUserEmail) {
        AppUser user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User was not found."));

        if (user.email().equalsIgnoreCase(normalizeEmail(currentUserEmail))) {
            throw new BusinessRuleException("No puedes eliminar tu propia cuenta desde esta sesion.");
        }

        AppUser currentUser = userRepositoryPort.findByEmail(normalizeEmail(currentUserEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found."));

        int reassignedOrders = customerOrderRepository.existsByCreatedByUserId(userId)
                ? customerOrderRepository.reassignCreatedOrders(userId, currentUser.id())
                : 0;

        userRepositoryPort.deleteById(userId);
    }

    private UserAccountView toView(AppUser user) {
        return new UserAccountView(user.id(), user.fullName(), user.email(), user.role());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
