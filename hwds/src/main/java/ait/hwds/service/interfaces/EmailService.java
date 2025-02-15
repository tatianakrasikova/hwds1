package ait.hwds.service.interfaces;



import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.User;

import java.util.List;

public interface EmailService {

    void sendConfirmationEmail(User user, String code);

    void sendBookingConfirmEmail(List<BookingDto> bookings, User user);
}
