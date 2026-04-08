package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;  // Para evitar warnings de null safety
import org.springframework.web.bind.annotation.*;
import com.foodtrack.spring.springboot_application.model.Producto;
import com.foodtrack.spring.springboot_application.service.ProductoService;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService service;  // Servicio de productos

    // Listar todos los productos
    @GetMapping
    public List<Producto> listar() {
        return service.listar();  // Retorna lista de productos
    }
    
    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        return service.obtenerPorId(id);
    }

    // Crear un nuevo producto
    @PostMapping
    public Producto guardar(@RequestBody @NonNull Producto p) {  // @NonNull evita warning
        return service.guardar(p);
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable @NonNull Integer id, @RequestBody @NonNull Producto p) {  // @NonNull evita warning
        return service.actualizar(id, p);
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        service.eliminar(id);
    }
}