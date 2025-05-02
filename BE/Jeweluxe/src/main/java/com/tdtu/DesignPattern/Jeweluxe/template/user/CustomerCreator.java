package com.tdtu.DesignPattern.Jeweluxe.template.user;

import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerCreator extends UserCreationProcessor {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerCreator(UserService userService, PasswordEncoder passwordEncoder) {
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

        newUser.setRole("ROLE_USER");
        newUser.setIsEnable(isEnable != null ? isEnable : true);
        newUser.setAccountNonLocked(true);
        newUser.setFailedAttempt(0);

    }

    @Override
    protected User saveNewUser(User user) {
        return userService.saveUser(user);
    }
}