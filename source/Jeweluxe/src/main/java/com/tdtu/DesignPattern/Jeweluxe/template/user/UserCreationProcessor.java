package com.tdtu.DesignPattern.Jeweluxe.template.user;

import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

public abstract class UserCreationProcessor {

    public final boolean processUserCreation(
            String name, String email, String password, String mobileNumber,
            String address, String city, String state, String pincode,
            Boolean isEnable, MultipartFile file,
            RedirectAttributes redirectAttributes, CommonUtil commonUtil
    ) {
        try {
            // 1a: Kiểm tra email tồn tại
            if (checkEmailExists(email)) {
                setPreConditionFailedMessage(email, redirectAttributes);
                return false;
            }

            // 1b: Kiểm tra các quy tắc nghiệp vụ khác (Hook)
            User tempUserForCheck = new User();
            tempUserForCheck.setEmail(email);
            if (!canPersist(tempUserForCheck)) {
                setPersistenceBlockedMessage(tempUserForCheck, redirectAttributes);
                return false;
            }

            // 2: Chuẩn bị User mới
            User newUser = new User();
            String imageName = determineImageName(file);
            prepareNewUser(newUser, name, email, password, mobileNumber, address, city, state, pincode, isEnable, imageName);


            // 3: Lưu User
            User savedUser = saveNewUser(newUser);

            // 4: Xử lý sau khi lưu
            if (!ObjectUtils.isEmpty(savedUser)) {
                if (!file.isEmpty() && commonUtil != null) {
                    try {
                        uploadProfileImage(commonUtil, file, imageName);
                    } catch (IOException e) {
                        setImageUploadWarningMessage(savedUser, redirectAttributes, e);
                    }
                }
                setSuccessMessage(savedUser, redirectAttributes);
                return true;
            } else {
                setSaveErrorMessage(newUser, redirectAttributes);
                return false;
            }


        } catch (Exception e) {
            setGenericErrorMessage(redirectAttributes, e);
            e.printStackTrace();
            return false;
        }
        // return false;
    }

    protected abstract boolean checkEmailExists(String email);

    protected abstract String encodePassword(String rawPassword);

    protected abstract void prepareNewUser(User newUser, String name, String email, String password, String mobileNumber,
                                           String address, String city, String state, String pincode,
                                           Boolean isEnable, String imageName);

    protected abstract User saveNewUser(User user);

    protected String determineImageName(MultipartFile file) {
        return file == null || file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
    }

    protected void uploadProfileImage(CommonUtil commonUtil, MultipartFile file, String imageName) throws IOException {
        if (commonUtil != null && file != null && !file.isEmpty() && !imageName.equals("default.jpg")) {
            commonUtil.uploadFile(file, "profile_img");
        }
    }

    protected boolean canPersist(User userToCheck) {
        return true;
    }

    protected void setPersistenceBlockedMessage(User userToCheck, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "User creation blocked due to business policy violation.");
    }

    protected void setPreConditionFailedMessage(String email, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "Email '" + email + "' already exists or is invalid.");
    }

    protected void setSuccessMessage(User savedUser, RedirectAttributes redirectAttributes) {
        String userTypeName = "User";
        if (savedUser != null && savedUser.getRole() != null) {
            userTypeName = savedUser.getRole().equals("ROLE_ADMIN") ? "Admin" : "Customer";
        }
        redirectAttributes.addFlashAttribute("succMsg", userTypeName + " '" + savedUser.getName() + "' added successfully.");
    }

    protected void setImageUploadWarningMessage(User savedUser, RedirectAttributes redirectAttributes, IOException e) {
        System.err.println("Warning: User '" + savedUser.getEmail() + "' saved, but profile image upload failed: " + e.getMessage());
        String userTypeName = "User";
        if (savedUser != null && savedUser.getRole() != null) {
            userTypeName = savedUser.getRole().equals("ROLE_ADMIN") ? "Admin" : "Customer";
        }
        redirectAttributes.addFlashAttribute("warnMsg", userTypeName + " saved, but profile image upload failed.");
    }

    protected void setSaveErrorMessage(User user, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", "Could not save user '" + user.getEmail() + "'. Internal server error.");
    }

    protected void setGenericErrorMessage(RedirectAttributes redirectAttributes, Exception e) {
        redirectAttributes.addFlashAttribute("errorMsg", "An unexpected server error occurred: " + e.getMessage());
    }
}