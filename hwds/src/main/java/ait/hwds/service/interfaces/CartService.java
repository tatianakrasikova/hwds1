package ait.hwds.service.interfaces;



import ait.hwds.model.dto.CartDto;
import ait.hwds.model.entity.Cart;
import ait.hwds.model.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CartService {

    CartDto getCart(User user);

    Cart getCartEntity(User authUser);

    void addArticleToCart(User authUser, Long articleId, LocalDate entryDate, LocalDate departureDate);

    void removeArticleFromCart(User authUser, Long articleId);

    BigDecimal getTotalPrice(User authUser);

    void clearUserCart(User authUser);

    void delete(Cart cart);
}
