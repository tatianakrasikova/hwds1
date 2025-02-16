package ait.hwds.service;


import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.User;
import ait.hwds.service.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MailTemplateService mailTemplateService;

    @Value("${mail.username}")
    private String fromAddress;

    @Value("${base-url}")
    private String baseUrl;

    public EmailServiceImpl(JavaMailSender mailSender, MailTemplateService mailTemplateService) {
        this.mailSender = mailSender;
        this.mailTemplateService = mailTemplateService;
    }

    @Override
    @Async
    public void sendConfirmationEmail(final User user, String confirmationCode) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }

        String emailText = generateEmailText(user, confirmationCode);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);

            helper.setTo(user.getEmail().toLowerCase().trim());
            helper.setSubject("Registration confirmation");

            helper.setText(emailText, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed. Please try again later.", e);
        }
    }

    @Override
    public void sendBookingConfirmEmail(List<BookingDto> bookings, User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }

        //todo удалить когда будут реальные изображения
        List<BookingDto> list = bookings.stream().peek(b -> b.setImageUrl("https://picsum.photos/400/300")).toList();
        String emailText = mailTemplateService.generateBookingConfirmationEmail(user, list);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail().toLowerCase().trim());
            helper.setSubject("Подтверждение бронирования");
            helper.setText(emailText, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка отправки email", e);
        }
    }

    private String generateEmailText(final User user, String confirmationCode) {
        return mailTemplateService.generateConfirmationEmail(user, confirmationCode, baseUrl);
    }
}
