package com.tdtu.DesignPattern.Jeweluxe.service.Implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import com.tdtu.DesignPattern.Jeweluxe.iterator.CartCollection;
import com.tdtu.DesignPattern.Jeweluxe.iterator.CartIterator;
import com.tdtu.DesignPattern.Jeweluxe.iterator.CartList;
import com.tdtu.DesignPattern.Jeweluxe.model.Cart;
import com.tdtu.DesignPattern.Jeweluxe.model.Product;
import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.repository.CartRepository;
import com.tdtu.DesignPattern.Jeweluxe.repository.ProductRepository;
import com.tdtu.DesignPattern.Jeweluxe.repository.UserRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.CartService;

import com.tdtu.DesignPattern.Jeweluxe.decorator.price.BaseOrderItemPriceCalculator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.GiftWrapDecorator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.InsuranceDecorator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.OrderItemPriceCalculator;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public Cart saveCart(Integer productId, Integer userId) {

        User User = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(User);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }
        Cart saveCart = cartRepository.save(cart);

        return saveCart;
    }

    @Override
    public List<Cart> getCartsByUser(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        CartCollection cartCollection = new CartList(carts);
        CartIterator iterator = cartCollection.createIterator();

        log.debug("Processing cart items for user {} using custom Iterator.", userId);

        while (iterator.hasNext()) {
            Cart cart = (Cart) iterator.next();

            double itemBaseTotalPrice = 0;
            if (cart.getProduct() != null && cart.getProduct().getDiscountPrice() != null && cart.getQuantity() != null) {
                itemBaseTotalPrice = cart.getProduct().getDiscountPrice() * cart.getQuantity();
            } else {
                log.warn("Custom Iterator: Cart item ID {} for user {} has invalid data.", cart.getId(), userId);
            }

            cart.setTotalPrice(itemBaseTotalPrice);
            cart.setDecoratedPrice(null);

            log.trace("Custom Iterator processed cart item ID {}: baseTotalPrice = {}", cart.getId(), itemBaseTotalPrice);
        }

        return carts;
    }

    @Override
    public Integer getCountCart(Integer userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateQuantity(String sy, Integer cid) {

        Cart cart = cartRepository.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }
    }

    @Override
    public Cart updateCartOption(Integer cartId, String optionType, boolean isChecked) {
        if (cartId == null || optionType == null) {
            log.error("Lỗi cập nhật tùy chọn giỏ hàng: cartId/ optionType là null.");
            throw new IllegalArgumentException("Cart ID và Option Type ko được null.");
        }

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()) {
            log.error("ko tìm thấy Cart với ID: {}", cartId);
            throw new IllegalArgumentException("ko tìm thấy mục giỏ hàng với ID: " + cartId);
        }

        Cart cart = cartOptional.get();

        if ("giftWrap".equalsIgnoreCase(optionType)) {
            cart.setWantsGiftWrap(isChecked);
            log.info("Cập nhật Gift Wrap cho Cart ID {}: {}", cartId, isChecked);
        } else if ("insurance".equalsIgnoreCase(optionType)) {
            cart.setWantsInsurance(isChecked);
            log.info("Cập nhật Insurance cho Cart ID {}: {}", cartId, isChecked);
        } else {
            log.error("Tùy chọn ko hợp lệ: {}", optionType);
            throw new IllegalArgumentException("Tùy chọn không hợp lệ: " + optionType + ". Chỉ chấp nhận 'giftWrap' hoặc 'insurance'.");
        }

        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            log.error("Lỗi khi lưu Cart ID {} sau khi cập nhật tùy chọn: {}", cartId, e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lưu cập nhật giỏ hàng.", e);
        }
    }

    @Override
    public double calculateDecoratedPrice(Cart cartItem) {
        if (cartItem == null || cartItem.getProduct() == null || cartItem.getProduct().getDiscountPrice() == null || cartItem.getQuantity() == null) {
            log.warn("Invalid Cart item provided for decorated price calculation: {}", cartItem != null ? cartItem.getId() : "null");
            return 0;
        }

        // 1: Tạo đối tượng OrderItem tạm thời từ Cart
        OrderItem tempItem = new OrderItem();
        tempItem.setPrice(cartItem.getProduct().getDiscountPrice());
        tempItem.setQuantity(cartItem.getQuantity());
        tempItem.setGiftWrap(cartItem.isWantsGiftWrap());
        tempItem.setInsurance(cartItem.isWantsInsurance());

        // 2: Bắt đầu bộ tính giá cơ bản
        OrderItemPriceCalculator calculator = new BaseOrderItemPriceCalculator();

        // 3: "Trang trí" dựa trên các tùy chọn
        if (tempItem.isGiftWrap()) {
            calculator = new GiftWrapDecorator(calculator);
        }
        if (tempItem.hasInsurance()) {
            calculator = new InsuranceDecorator(calculator);
        }

        // 4: Gọi phương thức calculatePrice
        return calculator.calculatePrice(tempItem);
    }


}

