package com.tdtu.DesignPattern.Jeweluxe.command;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackOrderCommand implements OrderCommand {
    private static final Logger log = LoggerFactory.getLogger(PackOrderCommand.class);
    private final OrderService orderService; // Receiver
    private final Integer orderItemId;      // Parameter

    public PackOrderCommand(OrderService orderService, Integer orderItemId) {
        this.orderService = orderService;
        this.orderItemId = orderItemId;
    }

    @Override
    public Object execute() throws Exception {
        log.info("Executing PackOrderCommand for OrderItem ID: {}", orderItemId);
        try {
            // Gọi phương thức packOrder của Receiver
            return orderService.packOrder(orderItemId);
        } catch (Exception e) {
            log.error("Error executing packOrder for ID {}: {}", orderItemId, e.getMessage());
            throw e; 
        }
    }

    // Optional: public void undo() { /* Logic hoàn tác */ }
}