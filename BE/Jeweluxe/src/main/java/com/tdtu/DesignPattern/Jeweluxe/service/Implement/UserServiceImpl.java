package com.tdtu.DesignPattern.Jeweluxe.service.Implement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.repository.UserRepository;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;
import com.tdtu.DesignPattern.Jeweluxe.util.AppConstant;
import com.tdtu.DesignPattern.Jeweluxe.util.CommonUtil; 

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired(required = false) 
    private CommonUtil commonUtil;

    @Override
    public User saveUser(User user) {
        if (user.getId() == null && user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getId() == null) {
            user.setRole("ROLE_USER");
            user.setIsEnable(true); 
            user.setAccountNonLocked(true); 
            user.setFailedAttempt(0); 
        }
        return userRepository.save(user);
    }

    @Override
    public User saveAdmin(User user) {
        if (user.getId() == null && user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers(String role) {
        return userRepository.findByRoleOrderByIdDesc(role);
    }

    @Override
    public Boolean updateAccountStatus(Integer id, Boolean status) {
        Optional<User> findByUser = userRepository.findById(id);
        if (findByUser.isPresent()) {
            User User = findByUser.get();
            User.setIsEnable(status);
            if (Boolean.TRUE.equals(status)) {
                User.setAccountNonLocked(true);
                User.setFailedAttempt(0);
                User.setLockTime(null);
            }
            userRepository.save(User);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempt(User user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public boolean unlockAccountTimeExpired(User user) {
        if (user.getLockTime() == null) return true;

        long lockTimeInMillis = user.getLockTime().getTime();
        long unlockTime = lockTimeInMillis + AppConstant.UNLOCK_DURATION_TIME;
        long currentTime = System.currentTimeMillis();

        if (unlockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFailedAttempt(0);
            userRepository.save(user);
        }
    }

    @Override
    public void updateUserResetToken(String email, String resetToken) {
        User findByEmail = userRepository.findByEmail(email);
        if (findByEmail != null) { // null
            findByEmail.setResetToken(resetToken);
            userRepository.save(findByEmail);
        } else {
            System.err.println("Cannot update reset token. User not found for email: " + email);
        }
    }

    @Override
    public User getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    //update user
    @Override
    public User updateUser(User user) {
        if(user == null || user.getId() == null) {
            System.err.println("Cannot update user with null object or null ID.");
            return null;
        }
        if(!userRepository.existsById(user.getId())) {
            System.err.println("User with ID " + user.getId() + " does not exist for update.");
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfile(User userFromForm, MultipartFile img) {
        try {
            Optional<User> dbUserOpt = userRepository.findById(userFromForm.getId());
            if (!dbUserOpt.isPresent()) {
                System.err.println("User not found for profile update: ID " + userFromForm.getId());
                return null;
            }
            User dbUser = dbUserOpt.get();
            
            dbUser.setName(userFromForm.getName());
            dbUser.setMobileNumber(userFromForm.getMobileNumber());
            dbUser.setAddress(userFromForm.getAddress());
            dbUser.setCity(userFromForm.getCity());
            dbUser.setState(userFromForm.getState());
            dbUser.setPincode(userFromForm.getPincode());
            
            String oldImageName = dbUser.getProfileImage();
            if (img != null && !img.isEmpty()) {
                String newImageName = img.getOriginalFilename();
                dbUser.setProfileImage(newImageName);
                if (commonUtil != null) {
                    boolean uploaded = commonUtil.uploadFile(img, "profile_img");
                    if(uploaded && oldImageName != null && !oldImageName.equals("default.jpg") && !oldImageName.equals(newImageName)){
                        commonUtil.deleteFile(oldImageName, "profile_img");
                    }
                } else {
                    System.err.println("CommonUtil not injected, cannot handle file operations automatically.");
                    // File saveFile = new ClassPathResource("static/img").getFile();
                    // Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + img.getOriginalFilename());
                    // Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            return userRepository.save(dbUser);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    

    @Override
    public User getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public boolean deleteUser(Integer id) {
        try {
            if (userRepository.existsById(id)) {
                // Lấy user để xóa ảnh (nếu cần và CommonUtil đã được inject)
                if (commonUtil != null) {
                    User userToDelete = getUserById(id);
                    if(userToDelete != null && userToDelete.getProfileImage() != null && !userToDelete.getProfileImage().equals("default.jpg")) {
                        commonUtil.deleteFile(userToDelete.getProfileImage(), "profile_img");
                    }
                }
                userRepository.deleteById(id);
                return true;
            } else {
                System.err.println("User with ID " + id + " not found for deletion.");
                return false;
            }
        } catch (DataIntegrityViolationException e) {
            System.err.println("Cannot delete user ID " + id + ". Integrity constraint violation.");
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting user ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
