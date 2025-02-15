package ait.hwds.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Schema(description = "Class that describes Departament")
public class DepartamentDto {

    private Long id;
    private String number;
    private String type;
    private BigDecimal price;
    private List<ArticleDto> articles = new ArrayList<>();
    private List<String> imageUrls;
    public String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ArticleDto> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDto> articles) {
        this.articles = articles;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        DepartamentDto departamentDto = (DepartamentDto) o;
        return Objects.equals(id, departamentDto.id) && Objects.equals(number, departamentDto.number) && Objects.equals(type, departamentDto.type) && Objects.equals(price, departamentDto.price) && Objects.equals(articles, departamentDto.articles) && Objects.equals(imageUrls, departamentDto.imageUrls) && Objects.equals(description, departamentDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, type, price, articles, imageUrls, description);
    }


}
