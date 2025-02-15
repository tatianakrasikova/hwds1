package ait.hwds.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Schema(description = "DTO representing the shopping cart")
public class CartDto {

    @Schema(description = "Unique identifier of the cart", example = "1")
    private Long id;

    @Schema(description = "List of article items in the cart")
    private List<CartItemArticleDto> articles;

    @Schema(description = "Count of articles in the cart", example = "2")
    private Long countArticles;

    @Schema(description = "Total price of articles in the cart", type = "number", example = "299.99")
    private BigDecimal totalPriceArticles;

    @JsonBackReference
    private UserDto userDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemArticleDto> getArticles() {
        return articles;
    }

    public void setArticles(List<CartItemArticleDto> articles) {
        this.articles = articles;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public Long getCountArticles() {
        return countArticles;
    }

    public void setCountArticles(Long countArticles) {
        this.countArticles = countArticles;
    }

    public BigDecimal getTotalPriceArticles() {
        return totalPriceArticles;
    }

    public void setTotalPriceArticles(BigDecimal totalPriceArticles) {
        this.totalPriceArticles = totalPriceArticles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDto cartDto = (CartDto) o;
        return Objects.equals(id, cartDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("CartDto: id - %d, cartItemArticles - %s, countArticles- %s, totalPriceArticles - %s",
                this.id, articles, countArticles, totalPriceArticles);
    }
}
