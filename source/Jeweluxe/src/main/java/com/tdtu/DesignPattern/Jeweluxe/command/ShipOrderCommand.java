package com.tdtu.DesignPattern.Jeweluxe.command;

import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShipOrderCommand implements OrderCommand {
    private static final Logger log = LoggerFactory.getLogger(ShipOrderCommand.class);
    private final OrderService orderService;
    private final Integer orderItemId;

    public ShipOrderCommand(OrderService orderService, Integer orderItemId) {
        this.orderService = orderService;
        this.orderItemId = orderItemId;
    }

    @Override
    public Object execute() throws Exception {
        log.info("Executing ShipOrderCommand for OrderItem ID: {}", orderItemId);
        try {
            // Gọi phương thức shipOrder của Receiver
            return orderService.shipOrder(orderItemId);
        } catch (Exception e) {
            log.error("Error executing shipOrder for ID {}: {}", orderItemId, e.getMessage());
            throw e;
        }
    }

    // Optional: public void undo() { /* Logic hoàn tác */ }
}