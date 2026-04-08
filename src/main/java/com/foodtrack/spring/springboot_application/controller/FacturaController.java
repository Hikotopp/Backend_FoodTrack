package com.foodtrack.spring.springboot_application.controller;

// Importaciones necesarias
import com.foodtrack.spring.springboot_application.model.Factura;
import com.foodtrack.spring.springboot_application.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;  // Para evitar warnings de null safety
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService service;  // Servicio de facturas

    // Listar todas las facturas
    @GetMapping
    public List<Factura> listar() {
        return service.listar();
    }

    // Obtener una factura por ID
    @GetMapping("/{id}")
    public Factura obtener(@PathVariable @NonNull Integer id) {  // @NonNull evita warning
        return service.obtenerPorId(id);
    }

    // Obtener factura por ID de pedido
    @GetMapping("/pedido/{pedidoId}")
    public Factura obtenerPorPedido(@PathVariable @NonNull Integer pedidoId) {  // @NonNull evita warning
        return service.obtenerPorPedido(pedidoId);
    }

    // Generar una nueva factura
    @PostMapping
    public Factura generar(@RequestBody @NonNull Factura factura) {  // @NonNull evita warning
        return service.generarFactura(factura);
    }
}