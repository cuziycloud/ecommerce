package com.tdtu.DesignPattern.Jeweluxe.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PostLoad;

import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;
import com.tdtu.DesignPattern.Jeweluxe.state.OrderStatusState;
import com.tdtu.DesignPattern.Jeweluxe.state.InProgressState;

import com.tdtu.DesignPattern.Jeweluxe.state.OrderReceivedState;
import com.tdtu.DesignPattern.Jeweluxe.state.ProductPackedState;
import com.tdtu.DesignPattern.Jeweluxe.state.ShippedState;     
import com.tdtu.DesignPattern.Jeweluxe.state.DeliveredState;
import com.tdtu.DesignPattern.Jeweluxe.state.CancelledState;

import jakarta.persistence.Transient;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient 
    private OrderStatusState currentState;

    @Column(name = "is_gift_wrap", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean giftWrap = false;

    @Column(name = "has_insurance", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean insurance = false;

    public void receive() {
        currentState.receiveOrder(this);
    }

    public void pack() {
        currentState.packOrder(this);
    }

    public void ship() {
        currentState.shipOrder(this);
    }

    public void deliver() {
        currentState.deliverOrder(this);
    }

    public void cancel() {
        currentState.cancelOrder(this);
    }

    public void changeState(OrderStatusState newState) {
        this.currentState = newState;
        this.status = newState.getStatus(); 
        System.out.println("Trạng thái đơn hàng " + this.id + " đổi thành: " + this.status.getName());
    }

    public OrderStatusState getCurrentState() {
        return currentState;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        if (this.currentState == null || this.currentState.getStatus() != status) {
            this.currentState = createStateFromEnum(status);
        }
    }

    private OrderStatusState createStateFromEnum(OrderStatus statusEnum) {
        if (statusEnum == null) return null;
        switch (statusEnum) {
            case IN_PROGRESS: return new InProgressState();
            case ORDER_RECEIVED: return new OrderReceivedState();
            case PRODUCT_PACKED: return new ProductPackedState();
            case OUT_FOR_DELIVERY: return new ShippedState();
            case DELIVERED: return new DeliveredState();
            case CANCELLED: return new CancelledState();
            default: throw new IllegalArgumentException("Trạng thái Enum ko xác định: " + statusEnum);
        }
    }

    @PostLoad
    public void initializeStateAfterLoad() {
         if (this.status != null && this.currentState == null) {
            this.currentState = createStateFromEnum(this.status);
         }
     }


    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private OrderAddress orderAddress;

    public boolean isGiftWrap() { return giftWrap; }
    public void setGiftWrap(boolean giftWrap) { this.giftWrap = giftWrap; }

    public boolean hasInsurance() { return insurance; }
    public void setInsurance(boolean insurance) { this.insurance = insurance; }


}