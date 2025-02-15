package ait.hwds.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


@Schema(description = "Class that describes Article")
public class ArticleDto {

    private Long id;
    private String number;
    private String type;
    private BigDecimal price;
    private Long departamentId;
    private List<String> imageUrls;
    public String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getDepartamentId() {
        return departamentId;
    }

    public void setDepartamentId(Long departamentId) {
        this.departamentId = departamentId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto articleDto = (ArticleDto) o;
        return Objects.equals(id, articleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("ArticleDto: id - %d, number - %s, type - %s, price - %s, departamentId - %d, imageUrls - %s, description - %s",
                id, number, type, price, departamentId, imageUrls, description);
    }
}
