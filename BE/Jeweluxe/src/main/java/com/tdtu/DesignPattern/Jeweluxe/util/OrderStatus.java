package com.tdtu.DesignPattern.Jeweluxe.util;

public enum OrderStatus {

    IN_PROGRESS(1, "In Progress"),
    ORDER_RECEIVED(2, "Order Received"),
    PRODUCT_PACKED(3, "Product Packed"),
    OUT_FOR_DELIVERY(4, "Out for Delivery"),
    DELIVERED(5, "Delivered"),
    CANCELLED(6, "Cancelled");

    private final Integer id;
    private final String name;

    OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static OrderStatus fromId(Integer id) {
        if (id == null) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        return null;
    }

}