package ait.hwds.repository;


import ait.hwds.model.entity.Article;
import ait.hwds.model.entity.Departament;
import ait.hwds.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.article = ?1")
    List<Image> findAllByArticle(Article article);

    @Query("select i from Image i where i.departament = ?1")
    List<Image> findAllByDepartament(Departament departament);
}
