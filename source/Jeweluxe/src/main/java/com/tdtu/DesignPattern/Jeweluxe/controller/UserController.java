package com.tdtu.DesignPattern.Jeweluxe.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tdtu.DesignPattern.Jeweluxe.strategy.shipping.ShippingCalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//decor
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.BaseOrderItemPriceCalculator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.GiftWrapDecorator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.InsuranceDecorator;
import com.tdtu.DesignPattern.Jeweluxe.decorator.price.OrderItemPriceCalculator;

import com.tdtu.DesignPattern.Jeweluxe.model.Cart;
import com.tdtu.DesignPattern.Jeweluxe.model.Category;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderRequest;
import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.service.CartService;
import com.tdtu.DesignPattern.Jeweluxe.service.CategoryService;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import com.tdtu.DesignPattern.Jeweluxe.service.ShippingService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShippingService shippingService;

    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            if (user != null) {
                m.addAttribute("user", user);
                Integer countCart = cartService.getCountCart(user.getId());
                m.addAttribute("countCart", countCart);
            }
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, Principal p, HttpSession session) {
        if (p == null) {
            session.setAttribute("errorMsg", "Please login to add items to cart.");
            return "redirect:/signin";
        }
        User currentUser = getLoggedInUserDetails(p);
        Cart saveCart = cartService.saveCart(pid, currentUser.getId());

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
        // return "redirect:/user/cart";
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {
        if (p == null) { return "redirect:/signin"; }

        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Tính toán giá decorate và tổng giá
        double totalDecoratedPrice = 0;
        for (Cart cart : carts) {
            if(cart.getDecoratedPrice() == null) {
                cart.setDecoratedPrice(cartService.calculateDecoratedPrice(cart));
            }
            totalDecoratedPrice += cart.getDecoratedPrice() != null ? cart.getDecoratedPrice() : 0;
        }

        m.addAttribute("carts", carts);
        m.addAttribute("totalOrderPrice", totalDecoratedPrice);

        return "/user/cart";
    }

    @GetMapping("/cartQuantity")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid, Principal p) { // Thêm Principal
        if (p == null) { return "redirect:/signin"; }
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    private User getLoggedInUserDetails(Principal p) {
        if (p == null) return null;
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {
        if (p == null) { return "redirect:/signin"; }

        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        double subTotal = 0;
        for(Cart cart : carts) {
            if(cart.getDecoratedPrice() == null) {
                cart.setDecoratedPrice(cartService.calculateDecoratedPrice(cart));
            }
            subTotal += cart.getDecoratedPrice() != null ? cart.getDecoratedPrice() : 0;
        }

        ShippingCalculationResult shippingResult = shippingService.calculateShipping(subTotal);
        double shippingFee = shippingResult.fee();
        String shippingDescription = shippingResult.description();
        double grandTotal = subTotal + shippingFee;

        m.addAttribute("carts", carts);
        m.addAttribute("subTotal", subTotal);
        m.addAttribute("shippingFee", shippingFee);
        m.addAttribute("shippingDescription", shippingDescription);
        m.addAttribute("grandTotal", grandTotal); // Tổng cuối cùng

        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p, RedirectAttributes redirectAttributes) { // Thêm RedirectAttributes
        if (p == null) { return "redirect:/signin"; }

        User user = getLoggedInUserDetails(p);
        try {
            orderService.saveOrder(user.getId(), request);
            redirectAttributes.addFlashAttribute("succMsg", "Order placed successfully!");
            return "redirect:/user/success";
        } catch (Exception e) {
            System.err.println("Error saving order: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Failed to place order. Please try again. Error: " + e.getMessage());
            return "redirect:/user/orders";
        }
    }

    @GetMapping("/success")
    public String loadSuccess() {
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p) {
        if (p == null) { return "redirect:/signin"; }
        User loginUser = getLoggedInUserDetails(p);
        List<OrderItem> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders", orders);
        return "/user/my_orders";
    }

    @PostMapping("/cancel-my-order")
    public String cancelMyOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes, Principal p) {
        if (p == null) { return "redirect:/signin"; }

        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("succMsg", "Your order (ID " + id + ") cancellation has been requested.");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Order not found for cancellation.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Cannot cancel order in its current state: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "An error occurred while cancelling the order.");
            e.printStackTrace();
        }
        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile(Principal p) {
        if (p == null) { return "redirect:/signin"; }
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user, @RequestParam MultipartFile img, HttpSession session, Principal p) {
        if (p == null) { return "redirect:/signin"; }
        User loggedInUser = getLoggedInUserDetails(p);
        if (loggedInUser == null || !loggedInUser.getId().equals(user.getId())) {
            session.setAttribute("errorMsg", "Unauthorized profile update attempt.");
            return "redirect:/user/profile";
        }

        User updateUserProfile = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(updateUserProfile)) {
            session.setAttribute("errorMsg", "Profile not updated");
        } else {
            session.setAttribute("succMsg", "Profile Updated");
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
                                 HttpSession session) {
        if (p == null) { return "redirect:/signin"; }

        User loggedInUserDetails = getLoggedInUserDetails(p);
        if (loggedInUserDetails == null) { // Thêm kiểm tra null
            session.setAttribute("errorMsg", "User not found.");
            return "redirect:/user/profile";
        }

        if (!passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword())) {
            session.setAttribute("errorMsg", "Current Password incorrect");
            return "redirect:/user/profile";
        }

        if (newPassword == null || newPassword.length() < 6) {
            session.setAttribute("errorMsg", "New password must be at least 6 characters long.");
            return "redirect:/user/profile";
        }

        String encodePassword = passwordEncoder.encode(newPassword);
        loggedInUserDetails.setPassword(encodePassword);
        try {
            User updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Password not updated !! Error in server");
            } else {
                session.setAttribute("succMsg", "Password Updated successfully"); // Sửa lỗi chính tả
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Error updating password: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/user/profile";
    }


    @PutMapping("/cart/updateOption")
    @ResponseBody
    public ResponseEntity<?> updateCartOption(
            @RequestParam Integer cartId,
            @RequestParam String optionType,
            @RequestParam Boolean isChecked,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Vui lòng đăng nhập"));
        }

        try {
            User currentUser = getLoggedInUserDetails(principal);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Không thể xác thực người dùng"));
            }

            List<Cart> currentCarts = cartService.getCartsByUser(currentUser.getId());
            Cart cartToUpdate = currentCarts.stream()
                    .filter(c -> c.getId() != null && c.getId().equals(cartId))
                    .findFirst()
                    .orElse(null);

            if (cartToUpdate == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "ko có quyền truy cập giỏ hàng này hoặc giỏ hàng không tồn tại"));
            }

            // 1. Lưu thay đổi boolean vào DB
            cartService.updateCartOption(cartId, optionType, isChecked);

            // 2. Lấy lại toàn bộ giỏ hàng sau khi cập nhật DB
            List<Cart> updatedCartList = cartService.getCartsByUser(currentUser.getId());

            // 3. Tìm lại item vừa cập nhật trong list mới
            Cart updatedCartItem = updatedCartList.stream()
                    .filter(c -> c.getId() != null && c.getId().equals(cartId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("ko tìm thấy cart item sau khi cập nhật"));

            // 4. Tính giá decorate mới cho item đó
            double newItemDecoratedPrice = cartService.calculateDecoratedPrice(updatedCartItem);

            // 5. Tính tổng giá decorate mới cho cả giỏ
            double newTotalDecoratedPrice = 0;
            for (Cart cart : updatedCartList) {
                if(cart.getDecoratedPrice() == null) {
                    cart.setDecoratedPrice(cartService.calculateDecoratedPrice(cart));
                }
                newTotalDecoratedPrice += cart.getDecoratedPrice() != null ? cart.getDecoratedPrice() : 0;
            }

            // 6. KQ
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Tùy chọn giỏ hàng đã được cập nhật.",
                    "cartId", cartId,
                    "newItemPrice", newItemDecoratedPrice,
                    "newTotalPrice", newTotalDecoratedPrice
            ));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Lỗi xảy ra trong updateCartOption API: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Lỗi hệ thống khi cập nhật giỏ hàng."));
        }
    }


}