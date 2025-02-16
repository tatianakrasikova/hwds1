package ait.hwds.repository;


import ait.hwds.model.entity.CartItemArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemArticleRepository extends JpaRepository<CartItemArticle, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemArticle c WHERE c.article.id = : articleId")
    boolean deleteArticleById(Long articleId);

}
