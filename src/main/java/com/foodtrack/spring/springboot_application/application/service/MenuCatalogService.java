package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.port.in.MenuUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MenuCatalogService implements MenuUseCase {

    private final MenuItemRepositoryPort menuItemRepositoryPort;

    public MenuCatalogService(MenuItemRepositoryPort menuItemRepositoryPort) {
        this.menuItemRepositoryPort = menuItemRepositoryPort;
    }

    @Override
    public List<MenuItem> listMenuItems() {
        return menuItemRepositoryPort.findAllActive().stream()
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
    }
}
