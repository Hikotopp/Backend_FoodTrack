package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import com.foodtrack.spring.springboot_application.model.Pedido;
import com.foodtrack.spring.springboot_application.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;  // Para evitar warnings de null safety
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService service;  // Servicio de pedidos

    // Listar todos los pedidos
    @GetMapping
    public List<Pedido> listar() {
        return service.listar();
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        return service.obtenerPorId(id);
    }

    // Crear un nuevo pedido
    @PostMapping
    public Pedido crear(@RequestBody @NonNull Pedido pedido) {  // @NonNull evita warning
        return service.crearPedido(pedido);
    }

    // Actualizar un pedido existente
    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable @NonNull Integer id, @RequestBody @NonNull Pedido pedido) {  // @NonNull evita warning
        return service.actualizar(id, pedido);
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        service.eliminar(id);
    }

    // Cambiar el estado de un pedido (PENDIENTE, EN PREPARACION, ENTREGADO, etc.)
    @PatchMapping("/{id}/estado")
    public Pedido cambiarEstado(@PathVariable @NonNull Integer id, @RequestParam @NonNull String estado) {  // @NonNull evita warning
        return service.cambiarEstado(id, estado);
    }
}