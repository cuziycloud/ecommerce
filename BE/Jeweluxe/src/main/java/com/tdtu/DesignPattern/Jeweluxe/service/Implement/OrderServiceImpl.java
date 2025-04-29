package com.tdtu.DesignPattern.Jeweluxe.service.Implement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//
import com.tdtu.DesignPattern.Jeweluxe.event.OrderCreatedEvent;
import com.tdtu.DesignPattern.Jeweluxe.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@Service // Đánh dấu lớp này là một Spring Bean, mặc định là Singleton.
        // Spring Container sẽ tạo duy nhất 1 instance của lớp này.
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemRepository orderRepository; // Nhận Singleton Repository

    @Autowired
    private CartRepository cartRepository;  // Nhận Singleton Repository

    @Autowired
    private CommonUtil commonUtil;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public OrderServiceImpl(OrderItemRepository orderRepository,
                            CartRepository cartRepository,
                            CommonUtil commonUtil,
                            ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.commonUtil = commonUtil;
        this.eventPublisher = eventPublisher;
    }

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid); //đang dùng Singleton instance
        if (carts == null || carts.isEmpty()) {
            System.out.println("Giỏ hàng trống, ko tạo đơn hàng cho user: " + userid);
            return;
        }
        List<OrderItem> savedOrders = new ArrayList<>();
        String newOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        for (Cart cart : carts) {
            if(cart.getProduct() == null || cart.getUser() == null) {
                System.out.println("Cart item không hợp lệ (thiếu product hoặc user) cho user: {}" + userid);
                continue;
            }
            OrderItem order = new OrderItem();

            // gán gtri
            order.setOrderId(newOrderId);
            order.setOrderDate(LocalDate.now());
            order.setProduct(cart.getProduct());
            //
            order.setPrice(cart.getProduct().getDiscountPrice());
            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());
            //order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setStatus(OrderStatus.IN_PROGRESS);
            order.setPaymentType(orderRequest.getPaymentType());

            //dchi
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

            order.setGiftWrap(cart.isWantsGiftWrap());
            order.setInsurance(cart.isWantsInsurance());

            order.initializeStateAfterLoad();

            try {
                OrderItem savedOrder = orderRepository.save(order); //đang dùng Singleton instance
                savedOrders.add(savedOrder);
            } catch (Exception e) {
                log.error("Lỗi khi lưu OrderItem (product ID: {}) cho user {}: {}",
                        (cart.getProduct() != null ? cart.getProduct().getId() : "N/A"),
                        userid, e.getMessage(), e);
                throw new Exception("Không thể lưu chi tiết đơn hàng cho sản phẩm " + (cart.getProduct() != null ? cart.getProduct().getTitle() : "không xác định") + ".", e);
            }

        }



        if (!savedOrders.isEmpty()) {
            OrderCreatedEvent orderEvent = new OrderCreatedEvent(this, savedOrders); // 'this' là OrderServiceImpl
            eventPublisher.publishEvent(orderEvent); // Phát sự kiện đi
            try {
                cartRepository.deleteAll(carts);
                log.info("Đã xóa giỏ hàng cho user {} sau khi tạo đơn hàng {}", userid, newOrderId);
            } catch (Exception e) {
                // Lỗi khi xóa giỏ hàng không nên làm ảnh hưởng đến việc đã tạo đơn hàng
                log.error("Lỗi khi xóa giỏ hàng cho user {}: {}", userid, e.getMessage(), e);
            }
        } else if (!carts.isEmpty()) {
            // Trường hợp không có item nào được lưu thành công (do lỗi hoặc tất cả cart items đều không hợp lệ)
            log.error("Không lưu được OrderItem nào cho user {}, đơn hàng {} không được tạo.", userid, newOrderId);
        }

//        // Xóa giỏ hàng
//        cartRepository.deleteAll(carts);
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
