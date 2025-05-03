package com.tdtu.DesignPattern.Jeweluxe.model;

import com.tdtu.DesignPattern.Jeweluxe.factory.OrderStatusStateCreator;
import com.tdtu.DesignPattern.Jeweluxe.state.OrderStatusState;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;
    private LocalDate orderDate;

    @ManyToOne
    private Product product;

    private Double price;
    private Integer quantity;

    @ManyToOne
    private User user;

    @Column(name = "is_gift_wrap", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean giftWrap = false;

    @Column(name = "has_insurance", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean insurance = false;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private OrderStatusState currentState;

    @Transient
    private static OrderStatusStateCreator stateCreatorInstance;

    @Transient
    private static final Logger log = LoggerFactory.getLogger(OrderItem.class);

    public static void setStateCreator(OrderStatusStateCreator creator) {
        if (OrderItem.stateCreatorInstance == null) {
            OrderItem.stateCreatorInstance = creator;
            log.info("OrderStatusStateCreator instance set for OrderItem.");
        } else {
            log.warn("Attempting to set OrderStatusStateCreator instance again.");
        }
    }

    public void receive() {
        if (currentState != null) {
            currentState.receiveOrder(this);
        } else {
            log.error("Cannot perform 'receive': currentState is null for OrderItem ID {}", this.id);
        }
    }

    public void pack() {
        if (currentState != null) {
            currentState.packOrder(this);
        } else {
            log.error("Cannot perform 'pack': currentState is null for OrderItem ID {}", this.id);
        }
    }

    public void ship() {
        if (currentState != null) {
            currentState.shipOrder(this);
        } else {
            log.error("Cannot perform 'ship': currentState is null for OrderItem ID {}", this.id);
        }
    }

    public void deliver() {
        if (currentState != null) {
            currentState.deliverOrder(this);
        } else {
            log.error("Cannot perform 'deliver': currentState is null for OrderItem ID {}", this.id);
        }
    }

    public void cancel() {
        if (currentState != null) {
            currentState.cancelOrder(this);
        } else {
            log.error("Cannot perform 'cancel': currentState is null for OrderItem ID {}", this.id);
        }
    }

    public void changeState(OrderStatusState newState) {
        if (newState == null) {
            log.error("Attempted to change state to null for OrderItem ID {}", this.id);
            return;
        }
        this.currentState = newState;
        this.status = newState.getStatus(); 
        log.info("Trạng thái đơn hàng {} đổi thành: {}", this.id, this.status.getName());
    }

    public OrderStatusState getCurrentState() {
        if (this.currentState == null && this.status != null) {
            initializeCurrentState();
        }
        return currentState;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        initializeCurrentState();
    }

    @PostLoad
    public void initializeStateAfterLoad() {
        initializeCurrentState();
    }

    private void initializeCurrentState() {
        if (this.status == null) {
            log.warn("Cannot initialize currentState: status is null for OrderItem ID {}", this.id);
            this.currentState = null;
            return;
        }
        if (OrderItem.stateCreatorInstance != null) {
            try {
                this.currentState = OrderItem.stateCreatorInstance.createStatusState(this.status);
                if (this.currentState == null) {
                    log.error("stateCreatorInstance returned null for status: {} on OrderItem ID {}", this.status, this.id);
                }
            } catch (IllegalArgumentException e) {
                log.error("Error creating state for status: {} on OrderItem ID {}. Error: {}", this.status, this.id, e.getMessage());
                this.currentState = null;
            }
        } else {
            log.error("CRITICAL: OrderStatusStateCreator instance is null. Cannot initialize state for OrderItem ID {}", this.id);
            this.currentState = null;
        }
    }


    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderAddress orderAddress;

    public boolean isGiftWrap() { return giftWrap; }
    public void setGiftWrap(boolean giftWrap) { this.giftWrap = giftWrap; }

    public boolean hasInsurance() { return insurance; }
    public void setInsurance(boolean insurance) { this.insurance = insurance; }
}