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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import com.tdtu.DesignPattern.Jeweluxe.model.Cart;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderAddress;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderRequest;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.repository.CartRepository;
import com.tdtu.DesignPattern.Jeweluxe.repository.OrderItemRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

@Service // Đánh dấu lớp này là một Spring Bean, mặc định là Singleton.
        // Spring Container sẽ tạo duy nhất 1 instance của lớp này.
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemRepository orderRepository; // Nhận Singleton Repository

    @Autowired
    private CartRepository cartRepository;  // Nhận Singleton Repository

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid); //đang dùng Singleton instance
        List<OrderItem> savedOrders = new ArrayList<>();

        for (Cart cart : carts) {

            OrderItem order = new OrderItem();

            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            //order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setStatus(OrderStatus.IN_PROGRESS);
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

            order.setStatus(OrderStatus.IN_PROGRESS);
            order.setOrderAddress(address);

            OrderItem savedOrder = orderRepository.save(order); //đang dùng Singleton instance

            savedOrders.add(savedOrder);
        }

        commonUtil.sendMailForOrderItem(savedOrders, "success");
    }

    @Override
    public List<OrderItem> getOrdersByUser(Integer userId) {
        List<OrderItem> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    @Transactional
    public OrderItem shipOrder(Integer orderItemId) {
        OrderItem orderItem = orderRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy OrderItem với ID: " + orderItemId));
        orderItem.ship(); 
        return orderRepository.save(orderItem); 
    }

    @Override
    @Transactional
    public OrderItem cancelOrder(Integer orderItemId) {
        OrderItem orderItem = orderRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy OrderItem với ID: " + orderItemId));
        orderItem.cancel();
        return orderRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem receiveOrder(Integer orderItemId) {
        OrderItem orderItem = orderRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy OrderItem với ID: " + orderItemId));
        orderItem.receive();

        return orderRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem packOrder(Integer orderItemId) {
        OrderItem orderItem = orderRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy OrderItem với ID: " + orderItemId));
        orderItem.pack();

        return orderRepository.save(orderItem);
    }

    @Override
    @Transactional
    public OrderItem deliverOrder(Integer orderItemId) {
        OrderItem orderItem = orderRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy OrderItem với ID: " + orderItemId));

        // Gọi 'deliver' trên OrderItem
        orderItem.deliver();

        return orderRepository.save(orderItem);
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
