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

        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();

        while (iterator.hasNext()) {
            Cart c = (Cart) iterator.next();
            Double totalPrice = c.getProduct().getDiscountPrice() * c.getQuantity();
            c.setTotalPrice(totalPrice);
            totalOrderPrice += totalPrice;
            c.setTotalPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
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
            return cartRepository.save(cart); // Lưu lại cart đã thay đổi
        } catch (Exception e) {
            log.error("Lỗi khi lưu Cart ID {} sau khi cập nhật tùy chọn: {}", cartId, e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lưu cập nhật giỏ hàng.", e);
        }
    }

}

