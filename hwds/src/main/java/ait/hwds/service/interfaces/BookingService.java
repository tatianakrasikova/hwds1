package ait.hwds.service.interfaces;



import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.Booking;
import ait.hwds.model.entity.User;

import java.util.List;

public interface BookingService {

    List<BookingDto> createBookingFromCart(User authUser);

    List<BookingDto> getBooking(User authUser);

    boolean hasActiveBookings(Long articleId);

    List<Booking> getPastBookings(Long articleId);

    void deletePastBookings(Long articleId);
    void deleteArticleWithoutFutureBookings(Long articleId);
}
