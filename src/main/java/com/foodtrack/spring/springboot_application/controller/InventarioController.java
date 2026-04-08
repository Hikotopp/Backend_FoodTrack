package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import com.foodtrack.spring.springboot_application.model.Inventario;
import com.foodtrack.spring.springboot_application.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;  // Para evitar warnings de null safety
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService service;  // Servicio de inventario

    // Listar todo el inventario
    @GetMapping
    public List<Inventario> listar() {
        return service.listar();
    }

    // Obtener registro de inventario por ID
    @GetMapping("/{id}")
    public Inventario obtener(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        return service.obtenerPorId(id);
    }

    // Crear registro de inventario
    @PostMapping
    public Inventario crear(@RequestBody @NonNull Inventario inventario) {  // @NonNull evita warning
        return service.guardar(inventario);
    }

    // Actualizar cantidad en inventario
    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable @NonNull Integer id, @RequestBody @NonNull Inventario inventario) {  // @NonNull evita warning
        return service.actualizar(id, inventario);
    }
}