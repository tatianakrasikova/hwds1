package ait.hwds.service;


import ait.hwds.exception.RestException;
import ait.hwds.model.dto.ArticleDto;
import ait.hwds.model.dto.CartDto;
import ait.hwds.model.dto.CartItemArticleDto;
import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Cart;
import ait.hwds.model.entity.CartItemArticle;
import ait.hwds.model.entity.User;
import ait.hwds.repository.CartItemArticleRepository;
import ait.hwds.repository.CartRepository;
import ait.hwds.repository.UserRepository;
import ait.hwds.service.interfaces.ArticleService;
import ait.hwds.service.interfaces.CartService;
import ait.hwds.service.mapping.CartMappingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class CartServiceImpl implements CartService {

    private final ArticleService articleService;
    private final CartRepository cartRepository;
    private final CartMappingService cartMappingService;
    private final UserRepository userRepository;
    private final CartItemArticleRepository cartItemArticleRepository;

    public CartServiceImpl(ArticleService articleService, CartRepository cartRepository, CartMappingService cartMappingService, UserRepository userRepository, CartItemArticleRepository cartItemArticleRepository) {
        this.articleService = articleService;
        this.cartRepository = cartRepository;
        this.cartMappingService = cartMappingService;
        this.userRepository = userRepository;
        this.cartItemArticleRepository = cartItemArticleRepository;
    }

    @Override
    public CartDto getCart(User authUser) {
        Cart userCart = getCartEntity(authUser);
        CartDto cartDto = cartMappingService.mapEntityToDto(userCart);

        long countArticles = userCart.getCartItemArticles().size();
        cartDto.setCountArticles(countArticles);

        BigDecimal totalPriceArticles = BigDecimal.ZERO;

        for (CartItemArticle cartItemArticle : userCart.getCartItemArticles()) {
            BigDecimal totalCostForArticle = calculateTotalCostForArticle(cartItemArticle);

            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(cartItemArticle.getArticle().getId());
            articleDto.setNumber(cartItemArticle.getArticle().getNumber());
            articleDto.setType(cartItemArticle.getArticle().getType());
            articleDto.setDepartamentId(cartItemArticle.getArticle().getDepartament().getId());
            // Устанавливаем пересчитанную цену
            articleDto.setPrice(totalCostForArticle);
            // добавляем BedDto в CartItemBedDto
            CartItemArticleDto cartItemArticleDto = new CartItemArticleDto();
            cartItemArticleDto.setId(cartItemArticle.getId());
            cartItemArticleDto.setEntryDate(cartItemArticle.getEntryDate());
            cartItemArticleDto.setDepartureDate(cartItemArticle.getDepartureDate());
            cartItemArticleDto.setArticle(articleDto);

            cartDto.getArticles().add(cartItemArticleDto);
            totalPriceArticles = totalPriceArticles.add(totalCostForArticle);
        }
        cartDto.setTotalPriceArticles(totalPriceArticles);
        return cartDto;
    }

    private BigDecimal calculateTotalCostForArticle(CartItemArticle cartItemArticle) {
        BigDecimal articlePrice = cartItemArticle.getArticle().getPrice();
        long daysBetween = ChronoUnit.DAYS.between(cartItemArticle.getEntryDate(), cartItemArticle.getDepartureDate());
        return articlePrice.multiply(BigDecimal.valueOf(daysBetween));
    }

    @Override
    public Cart getCartEntity(User authUser) {
        return cartRepository.findByUser(authUser)
                .orElseGet(() -> {
                            Cart userCart = new Cart();
                            userCart.setUser(authUser);
                            cartRepository.save(userCart);
                            authUser.setCart(userCart);
                            userRepository.save(authUser);
                            return userCart;
                        }
                );
    }

    @Override
    public void addArticleToCart(User authUser, Long articleId, LocalDate entryDate, LocalDate departureDate) {

        Article foundArticle = articleService.getArticleOrThrow(articleId);
        Cart userCart = getCartEntity(authUser);

        Optional<CartItemArticle> existingCartItem = userCart.getCartItemArticles()
                .stream()
                .filter(cartItemArticle -> cartItemArticle.getArticle().equals(foundArticle) &&
                        cartItemArticle.getEntryDate().equals(entryDate) &&
                        cartItemArticle.getDepartureDate().equals(departureDate))
                .findFirst();

        if (existingCartItem.isPresent()) {
            throw new RestException("The bed with id " + foundArticle.getId() + " for the dates from " + entryDate + " to " + departureDate + " is already in the cart.");
        }

        CartItemArticle newCartItemArticle = new CartItemArticle();
        newCartItemArticle.setArticle(foundArticle);
        newCartItemArticle.setCart(userCart);

        newCartItemArticle.setEntryDate(entryDate);
        newCartItemArticle.setDepartureDate(departureDate);

        cartItemArticleRepository.save(newCartItemArticle);
        userCart.getCartItemArticles().add(newCartItemArticle);
        cartRepository.save(userCart);
    }

    @Override
    public void removeArticleFromCart(User authUser, Long articleId) {
        Article foundArticle = articleService.getArticleOrThrow(articleId);
        Cart userCart = getCartEntity(authUser);

        List<CartItemArticle> cartItemArticles = userCart.getCartItemArticles();
        Optional<CartItemArticle> itemArticle = cartItemArticles.stream()
                .filter(cartItemArticle -> cartItemArticle.getArticle().equals(foundArticle))
                .findFirst();
        itemArticle.ifPresent(cartItemArticles::remove);
        cartRepository.save(userCart);
    }

    @Override
    public BigDecimal getTotalPrice(User authUser) {
        Cart userCart = getCartEntity(authUser);

        return userCart.getCartItemArticles()
                .stream()
                .map(this::calculateTotalCostForArticle)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void clearUserCart(User authUser) {
        Cart cartEntity = getCartEntity(authUser);
        cartEntity.getCartItemArticles().clear();

        cartRepository.save(cartEntity);
    }

    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}
