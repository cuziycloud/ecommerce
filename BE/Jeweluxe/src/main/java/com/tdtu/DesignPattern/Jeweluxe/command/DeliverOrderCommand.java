package com.tdtu.DesignPattern.Jeweluxe.command;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliverOrderCommand implements OrderCommand {
    private static final Logger log = LoggerFactory.getLogger(DeliverOrderCommand.class);
    private final OrderService orderService; // Receiver
    private final Integer orderItemId;      // Parameter

    public DeliverOrderCommand(OrderService orderService, Integer orderItemId) {
        this.orderService = orderService;
        this.orderItemId = orderItemId;
    }

    @Override
    public Object execute() throws Exception {
        log.info("Executing DeliverOrderCommand for OrderItem ID: {}", orderItemId);
        try {
            // Gọi phương thức deliverOrder của Receiver
            return orderService.deliverOrder(orderItemId);
        } catch (Exception e) {
            log.error("Error executing deliverOrder for ID {}: {}", orderItemId, e.getMessage());
            throw e;
        }
    }

    // Optional: public void undo() { /* Logic hoàn tác */ }
}