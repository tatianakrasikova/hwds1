package ait.hwds.model.dto;

import java.time.LocalDate;
import java.util.Objects;


public class CartItemArticleDto {

    private Long id;
    private LocalDate entryDate;
    private LocalDate departureDate;
    private ArticleDto article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public ArticleDto getArticle() {
        return article;
    }

    public void setArticle(ArticleDto bed) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemArticleDto that = (CartItemArticleDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("CartItemArticle: id - %d, entryDate - %s, departureDate - %s, article - %s",
                id, entryDate, departureDate, article);
    }
}
