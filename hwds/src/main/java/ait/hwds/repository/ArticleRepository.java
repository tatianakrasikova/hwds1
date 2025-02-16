package ait.hwds.repository;


import ait.hwds.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT b FROM Article b WHERE b.id NOT IN " +
            "(SELECT booking.article.id FROM Booking booking " +
            "WHERE booking.entryDate <= :departureDate AND booking.departureDate >= :entryDate)")
    List<Article> findAvailableBeds(@Param("entryDate") LocalDate entryDate,
                                    @Param("departureDate") LocalDate departureDate);

    boolean existsByDepartamentIdAndNumber(Long departamentId, String number);
}
