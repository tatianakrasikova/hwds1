package ait.hwds.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "article", uniqueConstraints = @UniqueConstraint(columnNames = {"departament_id", "article_number"}))
public class Article {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_number")
    @NotBlank(message = "Article number must not be empty or blank")
    @Pattern(regexp = "^\\S+$", message = "Article number must not contain spaces")
    @Size(min = 1, max = 10, message = "Article number must be between 1 and 10 characters")
    private String number;

    @Column(name = "article_type")
    @NotBlank(message = "Article type must not be empty or blank")
    @Size(min = 1, max = 30, message = "Article type must be between 1 and 30 characters")
    private String type;

    @Column(name = "article_price", nullable = false)
    @PositiveOrZero(message = "Price must be non-negative")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid number with at most one digit after the decimal")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "departament_id", nullable = false)
    private Departament departament;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "description")
    private String description;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Departament getDepartament() {
        return departament;
    }

    public void setDepartament(Departament departament) {
        this.departament = departament;
    }

    public Long getId() {
        return id;
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
        Article article= (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Article: id - %d, number - %s, type - %s, price - %s, description - %s, images - %s",
                id, number, type, price, description, images);
    }

   
}
