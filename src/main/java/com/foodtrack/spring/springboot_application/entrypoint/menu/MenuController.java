package com.foodtrack.spring.springboot_application.entrypoint.menu;

import com.foodtrack.spring.springboot_application.application.port.in.MenuUseCase;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Menu")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuUseCase menuUseCase;

    public MenuController(MenuUseCase menuUseCase) {
        this.menuUseCase = menuUseCase;
    }

    @GetMapping
    @Operation(summary = "List all active gourmet menu items")
    public ResponseEntity<List<MenuItem>> listMenuItems() {
        return ResponseEntity.ok(menuUseCase.listMenuItems());
    }
}
