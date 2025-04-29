package com.tdtu.DesignPattern.Jeweluxe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double baseTotalPrice;

    @Transient
    private Double decoratedPrice;

    @Column(name = "wants_gift_wrap", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean wantsGiftWrap = false;

    @Column(name = "wants_insurance", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean wantsInsurance = false;

    public boolean isWantsGiftWrap() { return wantsGiftWrap; }
    public void setWantsGiftWrap(boolean wantsGiftWrap) { this.wantsGiftWrap = wantsGiftWrap; }
    public boolean isWantsInsurance() { return wantsInsurance; }
    public void setWantsInsurance(boolean wantsInsurance) { this.wantsInsurance = wantsInsurance; }
    public Double getBaseTotalPrice() { return baseTotalPrice; }
    public void setBaseTotalPrice(Double baseTotalPrice) { this.baseTotalPrice = baseTotalPrice; }
    public Double getDecoratedPrice() { return decoratedPrice; }
    public void setDecoratedPrice(Double decoratedPrice) { this.decoratedPrice = decoratedPrice; }
}

