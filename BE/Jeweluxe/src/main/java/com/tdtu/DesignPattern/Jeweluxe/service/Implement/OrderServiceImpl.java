package com.tdtu.DesignPattern.Jeweluxe.service.Implement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tdtu.DesignPattern.Jeweluxe.model.Cart;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderAddress;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderRequest;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.repository.CartRepository;
import com.tdtu.DesignPattern.Jeweluxe.repository.OrderItemRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid);
        List<OrderItem> savedOrders = new ArrayList<>();

        for (Cart cart : carts) {

            OrderItem order = new OrderItem();

            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());
            address.setPincode(orderRequest.getPincode());

            order.setOrderAddress(address);

            // Lưu order vô db
            OrderItem savedOrder = orderRepository.save(order);

            // Thêm vào ds savedOrders
            savedOrders.add(savedOrder);
        }

        // Gửi email với ds orders đã lưu
        commonUtil.sendMailForOrderItem(savedOrders, "success");
    }

    @Override
    public List<OrderItem> getOrdersByUser(Integer userId) {
        List<OrderItem> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public OrderItem updateOrderStatus(Integer id, String status) {
        Optional<OrderItem> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            OrderItem OrderItem = findById.get();
            OrderItem.setStatus(status);
            OrderItem updateOrder = orderRepository.save(OrderItem);
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<OrderItem> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<OrderItem> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAll(pageable);

    }

    @Override
    public OrderItem getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

}
