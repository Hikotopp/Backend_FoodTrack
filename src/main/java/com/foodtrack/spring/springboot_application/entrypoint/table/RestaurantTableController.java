package com.foodtrack.spring.springboot_application.entrypoint.table;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.application.port.in.TableUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@Tag(name = "Tables")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantTableController {

    private final TableUseCase tableUseCase;

    public RestaurantTableController(TableUseCase tableUseCase) {
        this.tableUseCase = tableUseCase;
    }

    @GetMapping
    @Operation(summary = "List all restaurant tables with totals and item counts")
    public ResponseEntity<List<TableSummaryView>> listTables() {
        return ResponseEntity.ok(tableUseCase.listTables());
    }

    @GetMapping("/{tableId}")
    @Operation(summary = "Get the dashboard for a single table")
    public ResponseEntity<TableDashboardView> getDashboard(@PathVariable Long tableId) {
        return ResponseEntity.ok(tableUseCase.getDashboard(tableId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new restaurant table")
    public ResponseEntity<TableSummaryView> createTable(@Valid @RequestBody CreateTableRequest request) {
        TableSummaryView createdTable = tableUseCase.createTable(request.tableNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @DeleteMapping("/{tableId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a table without active orders")
    public ResponseEntity<Void> deleteTable(@PathVariable Long tableId) {
        tableUseCase.deleteTable(tableId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/status")
    @Operation(summary = "Update the operational status of a table")
    public ResponseEntity<TableSummaryView> updateTableStatus(
            @PathVariable Long tableId,
            @Valid @RequestBody UpdateTableStatusRequest request
    ) {
        return ResponseEntity.ok(tableUseCase.updateTableStatus(tableId, request.status()));
    }

    @PostMapping("/{tableId}/order-lines")
    @Operation(summary = "Add a menu item to the current table order")
    public ResponseEntity<TableDashboardView> addOrderLine(
            @PathVariable Long tableId,
            @Valid @RequestBody AddOrderLineRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                tableUseCase.addOrderLine(tableId, request.menuItemId(), request.quantity(), authentication.getName())
        );
    }

    @PatchMapping("/{tableId}/order-lines/{lineId}")
    @Operation(summary = "Update the quantity of an order line")
    public ResponseEntity<TableDashboardView> updateOrderLine(
            @PathVariable Long tableId,
            @PathVariable Long lineId,
            @Valid @RequestBody UpdateOrderLineRequest request
    ) {
        return ResponseEntity.ok(tableUseCase.updateOrderLine(tableId, lineId, request.quantity()));
    }

    @DeleteMapping("/{tableId}/order-lines/{lineId}")
    @Operation(summary = "Remove an order line from the table order")
    public ResponseEntity<TableDashboardView> removeOrderLine(
            @PathVariable Long tableId,
            @PathVariable Long lineId
    ) {
        return ResponseEntity.ok(tableUseCase.removeOrderLine(tableId, lineId));
    }

    @PostMapping("/{tableId}/close-order")
    @Operation(summary = "Close the current order and free the table")
    public ResponseEntity<TableDashboardView> closeOrder(@PathVariable Long tableId) {
        return ResponseEntity.ok(tableUseCase.closeOrder(tableId));
    }
}
