package com.foodtrack.spring.springboot_application.entrypoint;

import com.foodtrack.spring.springboot_application.domain.model.Pedido;
import com.foodtrack.spring.springboot_application.domain.port.in.PedidoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;

    public PedidoController(PedidoUseCase pedidoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoUseCase.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return pedidoUseCase.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido creado = pedidoUseCase.guardar(pedido);
        return ResponseEntity.created(URI.create("/api/pedidos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody String estado) {
        return ResponseEntity.ok(pedidoUseCase.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
