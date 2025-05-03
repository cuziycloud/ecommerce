package com.tdtu.DesignPattern.Jeweluxe.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tdtu.DesignPattern.Jeweluxe.model.User;
import com.tdtu.DesignPattern.Jeweluxe.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("daspabitra55@gmail.com", "Shooping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg=null;;



    public User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User User = userService.getUserByEmail(email);
        return User;
    }

    // adminController
    public boolean uploadFile(MultipartFile file, String subFolder) throws IOException {
        if (file == null || file.isEmpty()) {
            return false;
        }
        try {
            File saveFileDirectory = new ClassPathResource("static/img").getFile();
            Path targetDirectory = Paths.get(saveFileDirectory.getAbsolutePath(), subFolder);
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory);
            }
            Path targetPath = targetDirectory.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
            throw e;
        }
    }

    public boolean deleteFile(String filename, String subFolder) {
        if (filename == null || filename.isEmpty() || filename.equals("default.jpg")) {
            return true;
        }
        try {
            File saveFileDirectory = new ClassPathResource("static/img").getFile();
            Path targetPath = Paths.get(saveFileDirectory.getAbsolutePath(), subFolder, filename);
            return Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            System.err.println("Error deleting file " + filename + ": " + e.getMessage());
            return false;
        }
    }


}

