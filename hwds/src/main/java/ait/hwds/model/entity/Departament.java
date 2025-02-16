package ait.hwds.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Departament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Departament number must not be empty or blank")
    @Pattern(regexp = "^\\S+$", message = "Departament number must not contain spaces")
    @Size(min = 1, max = 10, message = "Departament number must be between 1 and 10 characters")
    private String number;

    @NotBlank(message = "Departament type must not be empty or blank")
    @Size(min = 1, max = 30, message = "Departament type must be between 1 and 30 characters")
    private String type;

    @OneToMany(mappedBy = "departament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "departament", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @Column(name = "description")
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
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
        if (!(o instanceof Departament departament)) return false;
        return Objects.equals(id, departament.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Departament{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", articles=" + articles +
                ", images=" + images +
                ", description='" + description + '\'' +
                '}';
    }



}
