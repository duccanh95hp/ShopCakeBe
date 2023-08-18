package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.dto.EmailDTO;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    UserRepository userRepository;
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailDTO emailDTO) {
        // Lấy thông tin người nhận, tiêu đề và nội dung từ EmailDTO

        String subject = emailDTO.getSubject();
        String text = emailDTO.getText();
        List<User> users = userRepository.findUsersWithSameBirthday();
        if(!users.isEmpty()) {
            for (User user : users) {
                String to = user.getEmail();
                emailService.sendEmail(to, subject, text);
            }
            return "Email sent successfully.";
        }
        return "Email sent fail.";

    }
}
