package com.foodtrack.spring.springboot_application.application.service;

import com.foodtrack.spring.springboot_application.application.model.TableDashboardView;
import com.foodtrack.spring.springboot_application.application.model.TableSummaryView;
import com.foodtrack.spring.springboot_application.application.port.in.TableUseCase;
import com.foodtrack.spring.springboot_application.application.port.out.CustomerOrderRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.exception.BusinessRuleException;
import com.foodtrack.spring.springboot_application.domain.exception.ResourceNotFoundException;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.CustomerOrder;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.domain.model.OrderLine;
import com.foodtrack.spring.springboot_application.domain.model.OrderStatus;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional
public class RestaurantTableApplicationService implements TableUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableApplicationService.class);

    private final RestaurantTableRepositoryPort restaurantTableRepositoryPort;
    private final CustomerOrderRepositoryPort customerOrderRepositoryPort;
    private final MenuItemRepositoryPort menuItemRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public RestaurantTableApplicationService(
            RestaurantTableRepositoryPort restaurantTableRepositoryPort,
            CustomerOrderRepositoryPort customerOrderRepositoryPort,
            MenuItemRepositoryPort menuItemRepositoryPort,
            UserRepositoryPort userRepositoryPort
    ) {
        this.restaurantTableRepositoryPort = restaurantTableRepositoryPort;
        this.customerOrderRepositoryPort = customerOrderRepositoryPort;
        this.menuItemRepositoryPort = menuItemRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }


    @Override
    public List<TableSummaryView> listTables() {
        List<RestaurantTable> tables = restaurantTableRepositoryPort.findAll().stream()
                .sorted(Comparator.comparingInt(RestaurantTable::tableNumber))
                .toList();

        Map<Long, CustomerOrder> openOrders = customerOrderRepositoryPort.findOpenByTableIds(
                tables.stream().map(RestaurantTable::id).toList()
        );

        return tables.stream()
                .map(table -> toSummary(table, openOrders.get(table.id())))
                .toList();
    }

    @Override
    public TableDashboardView getDashboard(Long tableId) {
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = customerOrderRepositoryPort.findOpenByTableId(tableId).orElse(null);
        return new TableDashboardView(table, currentOrder, getSortedMenu());
    }

    @Override
    public TableSummaryView createTable(int tableNumber) {
        restaurantTableRepositoryPort.findByTableNumber(tableNumber).ifPresent(existing -> {
            throw new BusinessRuleException("Table number already exists.");
        });

        RestaurantTable savedTable = restaurantTableRepositoryPort.save(
                new RestaurantTable(null, tableNumber, TableStatus.AVAILABLE)
        );
        return toSummary(savedTable, null);
    }

    @Override
    public void deleteTable(Long tableId) {
        customerOrderRepositoryPort.findOpenByTableId(tableId).ifPresent(order -> {
            throw new BusinessRuleException("Cannot delete a table with an open order.");
        });

        getTable(tableId);
        restaurantTableRepositoryPort.deleteById(tableId);
    }

    @Override
    public TableSummaryView updateTableStatus(Long tableId, TableStatus status) {
        RestaurantTable table = getTable(tableId);
        CustomerOrder openOrder = customerOrderRepositoryPort.findOpenByTableId(tableId).orElse(null);

        if (status == TableStatus.AVAILABLE && openOrder != null && !openOrder.lines().isEmpty()) {
            throw new BusinessRuleException("Close the current order before setting the table as available.");
        }

        RestaurantTable updatedTable = restaurantTableRepositoryPort.save(
                new RestaurantTable(table.id(), table.tableNumber(), status)
        );
        return toSummary(updatedTable, openOrder);
    }

    @Override
    public TableDashboardView addOrderLine(Long tableId, Long menuItemId, int quantity, String currentUserEmail) {
        RestaurantTable table = getTable(tableId);
        MenuItem menuItem = menuItemRepositoryPort.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found."));
        AppUser user = userRepositoryPort.findByEmail(normalizeEmail(currentUserEmail))
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found."));

        CustomerOrder currentOrder = customerOrderRepositoryPort.findOpenByTableId(tableId)
                .orElseGet(() -> createOpenOrder(tableId, user.id()));

        List<OrderLine> mergedLines = mergeOrderLine(currentOrder.lines(), menuItem, quantity);
        CustomerOrder recalculatedOrder = recalculateOrder(currentOrder, mergedLines, OrderStatus.OPEN);
        customerOrderRepositoryPort.save(recalculatedOrder);

        if (table.status() == TableStatus.AVAILABLE || table.status() == TableStatus.CLEANING) {
            restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.OCCUPIED));
        }

        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView updateOrderLine(Long tableId, Long lineId, int quantity) {
        CustomerOrder currentOrder = getOpenOrder(tableId);
        List<OrderLine> updatedLines = currentOrder.lines().stream()
                .map(line -> line.id().equals(lineId)
                        ? new OrderLine(
                        line.id(),
                        line.menuItemId(),
                        line.itemName(),
                        quantity,
                        line.unitPrice(),
                        calculateSubtotal(line.unitPrice(), quantity)
                )
                        : line)
                .toList();

        ensureLineExists(currentOrder.lines(), lineId);
        customerOrderRepositoryPort.save(recalculateOrder(currentOrder, updatedLines, OrderStatus.OPEN));
        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView removeOrderLine(Long tableId, Long lineId) {
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = getOpenOrder(tableId);

        List<OrderLine> remainingLines = currentOrder.lines().stream()
                .filter(line -> !line.id().equals(lineId))
                .toList();

        if (remainingLines.size() == currentOrder.lines().size()) {
            throw new ResourceNotFoundException("Order line was not found.");
        }

        if (remainingLines.isEmpty()) {
            customerOrderRepositoryPort.deleteById(currentOrder.id());
            restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.AVAILABLE));
            return getDashboard(tableId);
        }

        customerOrderRepositoryPort.save(recalculateOrder(currentOrder, remainingLines, OrderStatus.OPEN));
        return getDashboard(tableId);
    }

    @Override
    public TableDashboardView closeOrder(Long tableId) {
        RestaurantTable table = getTable(tableId);
        CustomerOrder currentOrder = getOpenOrder(tableId);

        if (currentOrder.lines().isEmpty()) {
            throw new BusinessRuleException("Cannot close an empty order.");
        }

        CustomerOrder closedOrder = recalculateOrder(currentOrder, currentOrder.lines(), OrderStatus.CLOSED);
        customerOrderRepositoryPort.save(closedOrder);
        restaurantTableRepositoryPort.save(new RestaurantTable(table.id(), table.tableNumber(), TableStatus.AVAILABLE));
        return getDashboard(tableId);
    }

    private RestaurantTable getTable(Long tableId) {
        return restaurantTableRepositoryPort.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found."));
    }

    private CustomerOrder getOpenOrder(Long tableId) {
        return customerOrderRepositoryPort.findOpenByTableId(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Open order was not found for this table."));
    }

    private CustomerOrder createOpenOrder(Long tableId, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return new CustomerOrder(
                null,
                tableId,
                userId,
                OrderStatus.OPEN,
                BigDecimal.ZERO,
                now,
                now,
                List.of()
        );
    }

    private List<OrderLine> mergeOrderLine(List<OrderLine> currentLines, MenuItem menuItem, int quantity) {
        List<OrderLine> mergedLines = new ArrayList<>();
        boolean merged = false;

        for (OrderLine line : currentLines) {
            if (line.menuItemId().equals(menuItem.id())) {
                int newQuantity = line.quantity() + quantity;
                mergedLines.add(new OrderLine(
                        line.id(),
                        line.menuItemId(),
                        line.itemName(),
                        newQuantity,
                        line.unitPrice(),
                        calculateSubtotal(line.unitPrice(), newQuantity)
                ));
                merged = true;
            } else {
                mergedLines.add(line);
            }
        }

        if (!merged) {
            mergedLines.add(new OrderLine(
                    null,
                    menuItem.id(),
                    menuItem.name(),
                    quantity,
                    menuItem.price(),
                    calculateSubtotal(menuItem.price(), quantity)
            ));
        }

        return mergedLines;
    }

    private CustomerOrder recalculateOrder(CustomerOrder order, List<OrderLine> lines, OrderStatus status) {
        BigDecimal total = lines.stream()
                .map(OrderLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CustomerOrder(
                order.id(),
                order.tableId(),
                order.createdByUserId(),
                status,
                total,
                order.createdAt(),
                LocalDateTime.now(),
                lines
        );
    }

    private void ensureLineExists(List<OrderLine> lines, Long lineId) {
        boolean exists = lines.stream().anyMatch(line -> line.id().equals(lineId));
        if (!exists) {
            throw new ResourceNotFoundException("Order line was not found.");
        }
    }

    private BigDecimal calculateSubtotal(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private List<MenuItem> getSortedMenu() {
        return menuItemRepositoryPort.findAllActive().stream()
                .sorted(Comparator
                        .comparingInt((MenuItem item) -> item.category().getDisplayOrder())
                        .thenComparing(MenuItem::name))
                .toList();
    }

    private TableSummaryView toSummary(RestaurantTable table, CustomerOrder currentOrder) {
        BigDecimal total = currentOrder == null ? BigDecimal.ZERO : currentOrder.total();
        int itemCount = currentOrder == null ? 0 : currentOrder.lines().stream()
                .mapToInt(OrderLine::quantity)
                .sum();

        return new TableSummaryView(
                table.id(),
                table.tableNumber(),
                table.status(),
                total,
                itemCount
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
