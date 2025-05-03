package com.tdtu.DesignPattern.Jeweluxe.template.user;

import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class AdminCreator extends UserCreationProcessor {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.allowed-domain:}")
    private String allowedAdminDomain;

    @Value("${app.admin.max-count:10}")
    private int maxAdminCount;

    @Autowired
    public AdminCreator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected boolean checkEmailExists(String email) {
        return userService.existsEmail(email);
    }

    @Override
    protected String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    protected void prepareNewUser(User newUser, String name, String email, String password, String mobileNumber,
                                  String address, String city, String state, String pincode,
                                  Boolean isEnable, String imageName) {
        // thtin chung
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(encodePassword(password));
        newUser.setMobileNumber(mobileNumber);
        newUser.setAddress(address);
        newUser.setCity(city);
        newUser.setState(state);
        newUser.setPincode(pincode);
        newUser.setProfileImage(imageName);
        newUser.setId(null);

        newUser.setRole("ROLE_ADMIN");
        newUser.setIsEnable(isEnable != null ? isEnable : false);
        newUser.setAccountNonLocked(true);
        newUser.setFailedAttempt(0);
    }

    @Override
    protected User saveNewUser(User user) {
        return userService.saveUser(user);
    }

    @Override
    protected boolean canPersist(User userToCheck) {
        if (allowedAdminDomain != null && !allowedAdminDomain.isEmpty() && !userToCheck.getEmail().toLowerCase().endsWith(allowedAdminDomain.toLowerCase())) {
            System.err.println("Admin email domain check failed for: " + userToCheck.getEmail() + ". Allowed: " + allowedAdminDomain);
            return false;
        }

        long currentAdminCount = userService.countUsersByRole("ROLE_ADMIN");
        if (currentAdminCount >= maxAdminCount) {
            System.err.println("Admin limit reached (" + currentAdminCount + "/" + maxAdminCount + "). Cannot create more admins.");
            return false;
        }

        return true;
    }

    @Override
    protected void setPersistenceBlockedMessage(User userToCheck, RedirectAttributes redirectAttributes) {
        if (allowedAdminDomain != null && !allowedAdminDomain.isEmpty() && !userToCheck.getEmail().toLowerCase().endsWith(allowedAdminDomain.toLowerCase())) {
            redirectAttributes.addFlashAttribute("errorMsg", "Admin email must belong to the domain: " + allowedAdminDomain);
        } else {
            long currentAdminCount = userService.countUsersByRole("ROLE_ADMIN");
            if (currentAdminCount >= maxAdminCount) {
                redirectAttributes.addFlashAttribute("errorMsg", "Cannot create new admin. Maximum admin limit (" + maxAdminCount + ") reached.");
            } else {
                super.setPersistenceBlockedMessage(userToCheck, redirectAttributes);
            }
        }
    }
}