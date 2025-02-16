package ait.hwds.model.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String s3Path;

    @Column(nullable = false)
    private String fileOriginName;

    @Column(nullable = false)
    private String s3BucketName;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "departament_id")
    private Departament departament;

    public Image() {
    }

    public Image(String s3Path, String fileOriginName, String s3BucketName, Article article) {
        this.s3Path = s3Path;
        this.fileOriginName = fileOriginName;
        this.s3BucketName = s3BucketName;
        this.article = article;
        this.departament = null;
    }

    public Image(String s3Path, String fileOriginName, String s3BucketName, Departament departament) {
        this.s3Path = s3Path;
        this.fileOriginName = fileOriginName;
        this.s3BucketName = s3BucketName;
        this.article = null;
        this.departament = departament;
    }

    public Long getId() {
        return id;
    }

    public String getS3Path() {
        return s3Path;
    }

    public void setS3Path(String s3Path) {
        this.s3Path = s3Path;
    }

    public String getFileOriginName() {
        return fileOriginName;
    }

    public void setFileOriginName(String fileOriginName) {
        this.fileOriginName = fileOriginName;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public void setS3BucketName(String s3BucketName) {
        this.s3BucketName = s3BucketName;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Departament getDepartament() {
        return departament;
    }

    public void setDepartament(Departament departament) {
        this.departament = departament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", s3Path='" + s3Path + '\'' +
                ", fileOriginName='" + fileOriginName + '\'' +
                ", s3BucketName='" + s3BucketName + '\'' +
                ", article=" + article +
                ", departament=" + departament +
                '}';
    }
}
