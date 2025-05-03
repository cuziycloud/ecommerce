package com.tdtu.DesignPattern.Jeweluxe.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderCommandInvoker {

    private static final Logger log = LoggerFactory.getLogger(OrderCommandInvoker.class);

    public Object invoke(OrderCommand command) throws Exception {
        log.info("Invoker executing command: {}", command.getClass().getSimpleName());
        try {
            return command.execute();
        } catch (Exception e) {
            log.error("Error executing command {}: {}", command.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }
}