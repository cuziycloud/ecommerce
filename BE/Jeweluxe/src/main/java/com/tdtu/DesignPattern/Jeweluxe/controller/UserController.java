package com.tdtu.DesignPattern.Jeweluxe.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;

import com.tdtu.DesignPattern.Jeweluxe.model.Cart;
import com.tdtu.DesignPattern.Jeweluxe.model.Category;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderRequest;
import com.tdtu.DesignPattern.Jeweluxe.model.OrderItem;
import com.tdtu.DesignPattern.Jeweluxe.model.User;
//import com.tdtu.DesignPattern.Jeweluxe.repository.UserRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.CartService;
import com.tdtu.DesignPattern.Jeweluxe.service.CategoryService;
import com.tdtu.DesignPattern.Jeweluxe.service.OrderService;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import com.tdtu.DesignPattern.Jeweluxe.util.OrderStatus;

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


    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User User = userService.getUserByEmail(email);
            m.addAttribute("user", User);
            Integer countCart = cartService.getCountCart(User.getId());
            m.addAttribute("countCart", countCart);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/product/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {

        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/cart";
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
        cartService.updateQuantity(sy, cid);
        return "redirect:/user/cart";
    }

    private User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User User = userService.getUserByEmail(email);
        return User;
    }

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {
        User user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartsByUser(user.getId());
        m.addAttribute("carts", carts);
        if (carts.size() > 0) {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
            m.addAttribute("orderPrice", orderPrice);
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception {
        // System.out.println(request);
        User user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(), request);

        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess() {
        return "/user/success";
    }

    @GetMapping("/user-orders")
    public String myOrder(Model m, Principal p) {
        User loginUser = getLoggedInUserDetails(p);
        List<OrderItem> orders = orderService.getOrdersByUser(loginUser.getId());
        m.addAttribute("orders", orders);
        return "/user/my_orders";
    }

    @PostMapping("/cancel-my-order")
    public String cancelMyOrderRequest(@RequestParam Integer id, RedirectAttributes redirectAttributes, Principal p) {
        if (p == null) {
            return "redirect:/signin";
        }

        try {
            User currentUser = getLoggedInUserDetails(p);


            orderService.cancelOrder(id); // Gọi service hủy đơn
            redirectAttributes.addFlashAttribute("succMsg", "Đơn hàng của bạn (ID " + id + ") đã được yêu cầu hủy.");

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy đơn hàng để hủy.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Không thể hủy đơn hàng ở trạng thái này: " + e.getMessage());
        }
        // catch (AccessDeniedException e) {
        //     redirectAttributes.addFlashAttribute("errorMsg", "Bạn không có quyền hủy đơn hàng này.");
        // }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Đã xảy ra lỗi khi hủy đơn hàng.");
            e.printStackTrace();
        }
        return "redirect:/user/user-orders";
    }

    @GetMapping("/profile")
    public String profile() {
        return "/user/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute User user, @RequestParam MultipartFile img, HttpSession session) {
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
        User loggedInUserDetails = getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            User updateUser = userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)) {
                session.setAttribute("errorMsg", "Password not updated !! Error in server");
            } else {
                session.setAttribute("succMsg", "Password Updated sucessfully");
            }
        } else {
            session.setAttribute("errorMsg", "Current Password incorrect");
        }

        return "redirect:/user/profile";
    }

}

