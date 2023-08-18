package com.example.shop_cake_be;

import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendEmail {
    @Autowired
    private EmailService emailService;
    @Autowired
    UserRepository userRepository;

    @Scheduled(cron = "0 1 0 * * ?")
    public void sendMail() {
        String subject = "Chương trình khuyến mại";
        String text = "Chúc mừng sinh nhật bạn ! Cảm ơn bạn đã đồng hành với cửa hàng ! Để tri ân, chúng tôi khuyến mại 20% cho tất cả " +
                "sản phẩm bạn mua hôm nay";
        List<User> users = userRepository.findUsersWithSameBirthday();
        if(!users.isEmpty()) {
            for (User user : users) {
                String to = user.getEmail();
                emailService.sendEmail(to, subject, text);
            }
        }

    }
}
