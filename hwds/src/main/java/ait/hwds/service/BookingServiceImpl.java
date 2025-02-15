package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.dto.BookingDto;
import ait.hwds.model.entity.Booking;
import ait.hwds.model.entity.Cart;
import ait.hwds.model.entity.CartItemArticle;
import ait.hwds.model.entity.User;
import ait.hwds.repository.BookingRepository;
import ait.hwds.service.interfaces.BookingService;
import ait.hwds.service.interfaces.CartService;
import ait.hwds.service.mapping.BookingMappingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CartService cartService;
    private final BookingMappingService bookingMappingService;
    private final BookingEmailService bookingEmailService;
    private final ArticleServiceImpl articleServiceImpl;


    public BookingServiceImpl(BookingRepository bookingRepository,
                              CartService cartService,
                              BookingMappingService bookingMappingService,
                              BookingEmailService bookingEmailService,
                              ArticleServiceImpl articleServiceImpl) {
        this.bookingRepository = bookingRepository;
        this.cartService = cartService;
        this.bookingMappingService = bookingMappingService;
        this.bookingEmailService = bookingEmailService;
        this.articleServiceImpl = articleServiceImpl;
    }

    @Override
    @Transactional
    public List<BookingDto> createBookingFromCart(User authUser) {

        Cart cart = cartService.getCartEntity(authUser);

        List<CartItemArticle> cartItemArticles = cart.getCartItemArticles();
        if (cartItemArticles == null || cartItemArticles.isEmpty()) {
            return new ArrayList<>();
        }

        List<Booking> bookingList = new ArrayList<>();
        for (CartItemArticle cartItemArticle : cartItemArticles) {
            Long articleId = cartItemArticle.getArticle().getId();
            LocalDate entryDate = cartItemArticle.getEntryDate();
            LocalDate departureDate = cartItemArticle.getDepartureDate();

            if (bookingRepository.isArticleBooked(articleId, entryDate, departureDate)) {
                throw new RestException("Кровать " + articleId + " уже забронирована на указанные даты");
            }

            Booking booking = new Booking(
                    entryDate,
                    departureDate,
                    cartService.getTotalPrice(authUser),
                    authUser,
                    cartItemArticle.getArticle()
            );
            bookingList.add(booking);
        }

        bookingRepository.saveAll(bookingList);
        authUser.setCart(null);
        cartService.delete(cart);

        List<BookingDto> mappedBookingDtos = bookingList
                .stream()
                .map(bookingMappingService::mapEntityToDto)
                .toList();
        bookingEmailService.sendBookingConfirmationEmail(mappedBookingDtos, authUser);
        return mappedBookingDtos;
    }

    @Override
    public List<BookingDto> getBooking(User authUser) {
        return bookingRepository.findAllByUser(authUser)
                .stream()
                .map(bookingMappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public boolean hasActiveBookings(Long articleId){
        return bookingRepository.existsByIdAndDepartureDateAfter(articleId, LocalDate.now());
    }

    @Override
    @Transactional
    public List<Booking> getPastBookings(Long articleId) {
        return bookingRepository.findBedByIdAndDepartureDateBefore(articleId, LocalDate.now());
    }

    @Override
    @Transactional
    public void deletePastBookings(Long articleId) {
        List<Booking> pastBookings = getPastBookings(articleId);
        bookingRepository.deleteAll(pastBookings);
    }

    @Override
    @Transactional
    public void deleteArticleWithoutFutureBookings(Long articleId) {
        if (!hasActiveBookings(articleId)) {
            deletePastBookings(articleId);
            articleServiceImpl.deleteArticleById(articleId);
        } else {
            throw new RestException(HttpStatus.NOT_FOUND, "Кровать забронирована на сегодня или в будущем");
        }
    }
}
