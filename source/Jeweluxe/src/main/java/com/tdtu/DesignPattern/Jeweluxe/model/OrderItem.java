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
import com.tdtu.DesignPattern.Jeweluxe.factory.OrderStatusStateFactory;

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

    @Column(name = "is_gift_wrap", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean giftWrap = false;

    @Column(name = "has_insurance", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean insurance = false;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient 
    private OrderStatusState currentState;

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

        // Gọi Factory để tạo đối tượng State tương ứng

        // không hề biết liệu createState sẽ trả về gì 
        // => chỉ biết rằng nó sẽ nhận được một đối tượng nào đó implement OrderStatusState. 
        // Lớp cụ thể nào được tạo ra là chi tiết ẩn đối với OrderItem
        // Quyết định này được ủy thác hoàn toàn cho phương thức createState bên trong lớp OrderStatusStateFactory
        this.currentState = OrderStatusStateFactory.createState(status); 

        if (this.currentState == null && status != null) {
            System.err.println("OrderStatusStateFactory trả về null cho     status: " + status + " đối với OrderItem ID: " + this.id);
        }
    }

    @PostLoad
    public void initializeStateAfterLoad() {
        if (this.status != null && this.currentState == null) {
            this.setStatus(this.status);
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