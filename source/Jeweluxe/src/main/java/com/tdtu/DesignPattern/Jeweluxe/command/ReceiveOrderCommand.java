package com.tdtu.DesignPattern.Jeweluxe.command;

import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveOrderCommand implements OrderCommand {
    private static final Logger log = LoggerFactory.getLogger(ReceiveOrderCommand.class);
    private final OrderService orderService; // Receiver
    private final Integer orderItemId;      // Parameter

    public ReceiveOrderCommand(OrderService orderService, Integer orderItemId) {
        this.orderService = orderService;
        this.orderItemId = orderItemId;
    }

    @Override
    public Object execute() throws Exception {
        log.info("Executing ReceiveOrderCommand for OrderItem ID: {}", orderItemId);
        try {
            // Gọi phương thức tương ứng của Receiver
            return orderService.receiveOrder(orderItemId); // OrderItem/Object
            //orderService.receiveOrder(orderItemId); // void
        } catch (Exception e) {
            log.error("Error executing receiveOrder for ID {}: {}", orderItemId, e.getMessage());
            throw e;
        }
    }
}