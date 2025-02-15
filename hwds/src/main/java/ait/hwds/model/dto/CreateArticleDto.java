package ait.hwds.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;


@Schema(description = "Class that describes Article")
public class CreateArticleDto {

    @NotBlank(message = "Article number must not be empty or blank")
    @Pattern(regexp = "^\\S+$", message = "Article number must not contain spaces")
    @Size(min = 1, max = 10, message = "Article number must be between 1 and 10 characters")
    private String number;

    @NotBlank(message = "Article type must not be empty or blank")
    @Size(min = 1, max = 30, message = "Article type must be between 1 and 30 characters")
    private String type;

    @PositiveOrZero(message = "Price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid number with at most one digit after the decimal")
    private BigDecimal price;

    private Long departamentId;

    private List<String> imageUrls;

    private String description;

    public Long getDepartamentId() {
        return departamentId;
    }

    public void setDepartamentId(Long departamentId) {
        this.departamentId = departamentId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Article: number - %s, type - %s, price - %s, departamentId - %s, description - %s, imageUrls - %s ",
                number, type, price, departamentId, imageUrls, description);
    }
}
