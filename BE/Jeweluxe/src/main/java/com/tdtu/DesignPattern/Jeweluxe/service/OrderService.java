package com.tdtu.DesignPattern.Jeweluxe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.tdtu.DesignPattern.Jeweluxe.model.OrderRequest;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

    public List<OrderItem> getOrdersByUser(Integer userId);

    public OrderItem updateOrderStatus(Integer id, String status);

    public List<OrderItem> getAllOrders();

    public OrderItem getOrdersByOrderId(String orderId);

    public Page<OrderItem> getAllOrdersPagination(Integer pageNo,Integer pageSize);
}

