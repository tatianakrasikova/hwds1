package ait.hwds.service;


import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.User;
import ait.hwds.service.interfaces.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingEmailService {

    private final EmailService emailService;

    public BookingEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendBookingConfirmationEmail(List<BookingDto> bookings, User user) {
        emailService.sendBookingConfirmEmail(bookings, user);
    }
}
