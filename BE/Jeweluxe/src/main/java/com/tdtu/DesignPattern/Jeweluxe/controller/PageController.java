package com.tdtu.DesignPattern.Jeweluxe.controller;

import com.tdtu.DesignPattern.Jeweluxe.model.Category;
import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.repository.UserRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.CartService;
import com.tdtu.DesignPattern.Jeweluxe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;
    @ModelAttribute
    public void commonUser(Principal p, Model m) {
        User user = null;
        if (p != null) {
            String email = p.getName();
            user = userRepository.findByEmail(email);
            if (user != null) {
                m.addAttribute("user", user);
            }
        }

        List<Category> categories = categoryService.getAllActiveCategory();
        m.addAttribute("categories", categories);

        if (user != null && "ROLE_USER".equals(user.getRole())) {
            try {
                Integer countCart = cartService.getCountCart(user.getId());
                m.addAttribute("countCart", countCart);
            } catch (Exception e) {
                m.addAttribute("countCart", 0);
                System.err.println("Error getting cart count for user " + user.getId() + ": " + e.getMessage());
            }
        } else {
            m.addAttribute("countCart", 0);
        }
    }



    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "about"; // templates/about.html
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        return "contact"; // templates/contact.html
    }

    @PostMapping("/contact")
    public String handleContactForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            Model model) {

        System.out.println("Contact Form Submission:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);

        model.addAttribute("successMessage", "Thank you, " + name + "! Your message has been sent successfully.");

        return "contact";
    }
}